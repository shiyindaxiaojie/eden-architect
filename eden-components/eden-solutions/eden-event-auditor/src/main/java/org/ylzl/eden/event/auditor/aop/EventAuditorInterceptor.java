package org.ylzl.eden.event.auditor.aop;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.jetbrains.annotations.NotNull;
import org.springframework.aop.support.AopUtils;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.StopWatch;
import org.ylzl.eden.commons.lang.StringUtils;
import org.ylzl.eden.commons.lang.Strings;
import org.ylzl.eden.event.auditor.EventAuditor;
import org.ylzl.eden.event.auditor.EventSender;
import org.ylzl.eden.event.auditor.builder.EventSenderBuilder;
import org.ylzl.eden.event.auditor.config.EventAuditorConfig;
import org.ylzl.eden.spring.framework.expression.function.CustomFunctionRegistrar;
import org.ylzl.eden.event.auditor.model.AuditingEvent;
import org.ylzl.eden.extension.ExtensionLoader;
import org.ylzl.eden.spring.framework.expression.SpelEvaluationContext;
import org.ylzl.eden.spring.framework.expression.SpelExpressionEvaluator;
import org.ylzl.eden.spring.framework.json.support.JSONHelper;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 审计标记拦截器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
@Slf4j
public class EventAuditorInterceptor implements MethodInterceptor {

	private static final String RETURN = "_return";

	private static final String ERROR_MSG = "_errorMsg";

	private final EventAuditorConfig eventAuditorConfig;

	/**
	 * 方法调用拦截处理
	 *
	 * @param invocation 方法调用元信息
	 * @return 返回值
	 * @throws Throwable 异常
	 */
	@Override
	public Object invoke(@NotNull MethodInvocation invocation) throws Throwable {
		if (AopUtils.isAopProxy(invocation.getThis())) {
			return invocation.proceed();
		}

		Method method = invocation.getMethod();
		EventAuditor[] eventAuditors;
		try {
			eventAuditors = method.getAnnotationsByType(EventAuditor.class);
		} catch (Throwable throwable) {
			// 如果解析异常，直接执行返回
			return invocation.proceed();
		}

		List<AuditingEvent> events = Lists.newArrayList();
		events.addAll(parseList(invocation, eventAuditors, true));

		// 执行耗时
		String watchId = invocation.getClass() + Strings.DOT + invocation.getMethod().getName();
		StopWatch stopWatch = new StopWatch(watchId);
		stopWatch.start();
		Object result = null;
		boolean success = true;
		long executionCost = 0L;
		String errorMsg = null;
		try {
			result = invocation.proceed();
		} catch (Throwable e) {
			if (stopWatch.isRunning()) {
				stopWatch.stop();
				executionCost = stopWatch.getTotalTimeMillis();
			}
			success = false;
			errorMsg = e.getMessage();
			SpelEvaluationContext.setVariable(ERROR_MSG, errorMsg);	// 异常信息
			throw e;
		} finally {
			if (stopWatch.isRunning()) {
				stopWatch.stop();
				executionCost = stopWatch.getTotalTimeMillis();
			}
			SpelEvaluationContext.setVariable(RETURN, result);	// 返回值处理
			events.addAll(parseList(invocation, eventAuditors, false));
			for (AuditingEvent event : events) {
				event.setSuccess(success);
				event.setExecutionCost(executionCost);
				if (errorMsg != null) {
					event.setThrowable(errorMsg);
				}
			}

			send(events);
			SpelEvaluationContext.remove(); // 清理当前线程变量
		}
		return result;
	}

	/**
	 * 发送审计事件
	 *
	 * @param events 审计事件列表
	 */
	private void send(List<AuditingEvent> events) {
		String senderType = eventAuditorConfig.getSender().getSenderType();
		EventSenderBuilder eventSenderBuilder = ExtensionLoader.getExtensionLoader(EventSenderBuilder.class).getExtension(senderType);
		eventSenderBuilder.setEventAuditorConfig(eventAuditorConfig);
		EventSender eventSender = eventSenderBuilder.build();
		eventSender.send(events);
	}

	/**
	 * 解析 {@code AuditingEvent} 列表
	 *
	 * @param invocation 方法调用元信息
	 * @param eventAuditors 审计注解
	 * @param evalBeforeInvoke 是否在调用方法之前提前解析
	 * @return {@code AuditingEvent} 列表
	 */
	private List<AuditingEvent> parseList(@NotNull MethodInvocation invocation, EventAuditor[] eventAuditors,
										  boolean evalBeforeInvoke) {
		List<AuditingEvent> events = Lists.newArrayList();
		for (EventAuditor eventAuditor : eventAuditors) {
			if (eventAuditor.evalBeforeInvoke() == evalBeforeInvoke) {
				AuditingEvent auditingEvent = this.parseModel(eventAuditor, invocation);
				if (auditingEvent != null) {
					events.add(auditingEvent);
				}
			}
		}
		return events;
	}

	/**
	 * 解析模型
	 *
	 * @param eventAuditor 事件审计注解
	 * @param invocation 方法调用元信息
	 * @return 目标模型
	 */
	private AuditingEvent parseModel(EventAuditor eventAuditor, MethodInvocation invocation) {
		EvaluationContext context = SpelEvaluationContext.getContext();
		CustomFunctionRegistrar.register((StandardEvaluationContext) context);
		Method method = invocation.getMethod();
		String[] parameterNames = SpelExpressionEvaluator.getParameterNameDiscoverer().getParameterNames(method);
		Object[] arguments = invocation.getArguments();
		if (parameterNames != null) {
			int len = parameterNames.length;
			for (int i = 0; i < len; i++) {
				context.setVariable(parameterNames[i], arguments[i]);
			}
		}

		ExpressionParser parser = SpelExpressionEvaluator.getExpressionParser();
		if (StringUtils.isNotBlank(eventAuditor.condition())) {
			Expression expression = parser.parseExpression(eventAuditor.condition());
			if (!Boolean.TRUE.equals(expression.getValue(context, Boolean.class))) {
				return null;
			}
		}

		AuditingEvent auditingEvent = new AuditingEvent();
		if (StringUtils.isNotBlank(eventAuditor.operator())) {
			Expression expression = parser.parseExpression(eventAuditor.operator());
			auditingEvent.setOperator(expression.getValue(context, String.class));
		}

		if (StringUtils.isNotBlank(eventAuditor.role())) {
			Expression expression = parser.parseExpression(eventAuditor.role());
			auditingEvent.setRole(expression.getValue(context, String.class));
		}

		if (StringUtils.isNotBlank(eventAuditor.bizScenario())) {
			Expression expression = parser.parseExpression(eventAuditor.bizScenario());
			auditingEvent.setBizScenario(expression.getValue(context, String.class));
		}

		if (StringUtils.isNotBlank(eventAuditor.content())) {
			Expression expression = parser.parseExpression(eventAuditor.content());
			auditingEvent.setContent(expression.getValue(context, String.class));
		}

		if (StringUtils.isNotBlank(eventAuditor.extra())) {
			Expression expression = parser.parseExpression(eventAuditor.extra());
			Object extra = expression.getValue(context, Object.class);
			auditingEvent.setExtra(extra instanceof String? (String) extra : JSONHelper.json().toJSONString(extra));
		}
		return auditingEvent;
	}
}
