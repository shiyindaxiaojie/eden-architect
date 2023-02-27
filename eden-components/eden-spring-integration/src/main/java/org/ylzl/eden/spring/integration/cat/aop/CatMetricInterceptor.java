package org.ylzl.eden.spring.integration.cat.aop;

import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.jetbrains.annotations.NotNull;
import org.springframework.aop.support.AopUtils;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.ylzl.eden.commons.lang.StringUtils;
import org.ylzl.eden.commons.lang.Strings;
import org.ylzl.eden.spring.framework.expression.SpelEvaluationContext;
import org.ylzl.eden.spring.framework.expression.SpelExpressionEvaluator;
import org.ylzl.eden.spring.integration.cat.CatClient;
import org.ylzl.eden.spring.integration.cat.CatMetric;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

/**
 * CatLogMetricForCount 拦截器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
@Slf4j
public class CatMetricInterceptor implements MethodInterceptor {

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
		CatMetric[] catMetrics;
		try {
			catMetrics = method.getAnnotationsByType(CatMetric.class);
		} catch (Throwable throwable) {
			// 如果解析异常，直接执行返回
			return invocation.proceed();
		}

		try {
			return invocation.proceed();
		} finally {
			logMetric(catMetrics, invocation);
		}
	}

	private void logMetric(CatMetric[] catMetrics, MethodInvocation invocation) {
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
		for (CatMetric metric : catMetrics) {
			String name = metric.name();
			if (StringUtils.isNotBlank(name)) {
				Expression expression = parser.parseExpression(metric.name());
				name = expression.getValue(context, String.class);
			} else {
				name = Objects.requireNonNull(invocation.getThis()).getClass().getSimpleName() +
					Strings.DOT + method.getName();
			}

			Map<String, String> tags = Maps.newHashMap();
			if (metric.tags() != null && metric.tags().length > 0) {
				Arrays.stream(metric.tags()).forEach(tag -> {
					String[] arr = tag.split(Strings.EQ);
					Expression expression = parser.parseExpression(arr[1]);
					tags.put(arr[0], expression.getValue(context, String.class));
				});
			}

			if (StringUtils.isNotBlank(metric.quantity())) {
				Expression expression = parser.parseExpression(metric.quantity());
				CatClient.logMetricForCount(name, expression.getValue(context, Integer.class), tags);
			}
			if (StringUtils.isNotBlank(metric.durationInMillis())) {
				Expression expression = parser.parseExpression(metric.durationInMillis());
				CatClient.logMetricForDuration(name, expression.getValue(context, Long.class), tags);
			}
			if (StringUtils.isNotBlank(metric.quantity())) {
				Expression expression = parser.parseExpression(metric.sum());
				CatClient.logMetricForSum(name, expression.getValue(context, Double.class));
			}
			if (metric.count() > 0) {
				CatClient.logMetricForCount(name);
			}
		}
	}
}
