package org.ylzl.eden.event.auditor.aop;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.jetbrains.annotations.NotNull;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.util.StopWatch;
import org.ylzl.eden.commons.lang.StringUtils;
import org.ylzl.eden.commons.lang.Strings;
import org.ylzl.eden.event.auditor.EventAuditor;
import org.ylzl.eden.event.auditor.model.AuditingEvent;
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
@Slf4j
public class EventAuditorInterceptor implements MethodInterceptor, BeanFactoryAware, SmartInitializingSingleton {

	private static final String RETURN = "_return";

	private static final String ERROR_MSG = "_errorMsg";

	private BeanFactory beanFactory;

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
		try {
			result = invocation.proceed();
			stopWatch.stop();
		} catch (Throwable e) {
			if (stopWatch.isRunning()) {
				stopWatch.stop();
			}

		} finally {
			long executionCost = stopWatch.getTotalTimeMillis();

			SpelEvaluationContext.setVariable(RETURN, result);	// 返回值处理
		}
		events.addAll(parseList(invocation, eventAuditors, false));
		return result;
	}

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

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	@Override
	public void afterSingletonsInstantiated() {

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
