package org.ylzl.eden.event.auditor.expression;

import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.context.expression.CachedExpressionEvaluator;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TODO
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class EventAuditorExpressionEvaluator extends CachedExpressionEvaluator {

	private final Map<AnnotatedElementKey, Method> targetMethodCache = new ConcurrentHashMap<>(64);

	private final Map<ExpressionKey, Expression> expressionCache = new ConcurrentHashMap<>(64);

	public EvaluationContext createEvaluationContext(Method method, Object[] args, Class<?> targetClass,
													 Object result, String errorMsg, BeanFactory beanFactory) {
		AnnotatedElementKey methodKey = new AnnotatedElementKey(method, targetClass);
		Method targetMethod = targetMethodCache.computeIfAbsent(methodKey,
			k -> AopUtils.getMostSpecificMethod(method, targetClass));
		EventAuditorEvaluationContext evaluationContext = new EventAuditorEvaluationContext(
			null, targetMethod, args, getParameterNameDiscoverer(), result, errorMsg);
		if (beanFactory != null) {
			evaluationContext.setBeanResolver(new BeanFactoryResolver(beanFactory));
		}
		return evaluationContext;
	}

	public Object parseExpression(String conditionExpression, AnnotatedElementKey methodKey,
								  EvaluationContext evalContext) {
		return getExpression(this.expressionCache, methodKey, conditionExpression).getValue(evalContext, Object.class);
	}
}
