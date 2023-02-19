package org.ylzl.eden.spring.integration.cat.aop;

import com.dianping.cat.Cat;
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
import org.ylzl.eden.commons.lang.StringUtils;
import org.ylzl.eden.commons.lang.Strings;
import org.ylzl.eden.spring.framework.expression.SpelEvaluationContext;
import org.ylzl.eden.spring.framework.expression.SpelExpressionEvaluator;
import org.ylzl.eden.spring.framework.expression.function.CustomFunctionRegistrar;
import org.ylzl.eden.spring.integration.cat.CatLogMetricForCount;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * CatLogMetricForCount 拦截器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
@Slf4j
public class CatLogMetricForCountInterceptor implements MethodInterceptor {

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
		CatLogMetricForCount[] catLogMetricForCounts;
		try {
			catLogMetricForCounts = method.getAnnotationsByType(CatLogMetricForCount.class);
		} catch (Throwable throwable) {
			// 如果解析异常，直接执行返回
			return invocation.proceed();
		}

		try {
			return invocation.proceed();
		} finally {
			logMetricForCount(catLogMetricForCounts, invocation);
		}
	}

	private void logMetricForCount(CatLogMetricForCount[] catLogMetricForCounts, MethodInvocation invocation) {
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
		for (CatLogMetricForCount catLogMetricForCount : catLogMetricForCounts) {
			String name = catLogMetricForCount.name();
			if (StringUtils.isNotBlank(name)) {
				if (catLogMetricForCount.enableSpEL()) {
					Expression expression = parser.parseExpression(catLogMetricForCount.name());
					name = expression.getValue(context, String.class);
				}
			} else {
				name = Objects.requireNonNull(invocation.getThis()).getClass().getSimpleName() +
					Strings.DOT + method.getName();
			}
			Cat.logMetricForCount(name, catLogMetricForCount.count());
		}
	}
}
