package org.ylzl.eden.spring.integration.cat.annotations;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.annotation.AnnotationUtils;
import org.ylzl.eden.commons.collections.CollectionUtils;
import org.ylzl.eden.commons.lang.StringUtils;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Cat.logMetricForCount 注解处理器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Slf4j
public class CatLogMetricForCountAnnotationProcessor implements BeanPostProcessor, PriorityOrdered {

	@Override
	public Object postProcessAfterInitialization(@NotNull Object bean, @NotNull String beanName) throws BeansException {
		Class<?> targetClass = AopProxyUtils.ultimateTargetClass(bean);
		Map<Method, CatLogMetricForCount> annotatedMethods = MethodIntrospector.selectMethods(targetClass,
			(MethodIntrospector.MetadataLookup<CatLogMetricForCount>) method ->
				AnnotationUtils.getAnnotation(method, CatLogMetricForCount.class));
		if (CollectionUtils.isNotEmpty(annotatedMethods)) {
			for (Map.Entry<Method, CatLogMetricForCount> entry : annotatedMethods.entrySet()) {
				return processMetricForCount(bean, entry.getKey(), entry.getValue());
			}
		}
		return bean;
	}

	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE;
	}

	private Object processMetricForCount(Object bean, Method method, CatLogMetricForCount catLogMetricForCount) {
		if (StringUtils.isEmpty(catLogMetricForCount.name())) {
			log.warn("@MetricForCount annotation on '{}' name can't be null or empty", method.getName());
			return bean;
		}
		if (catLogMetricForCount.count() <= 0) {
			log.warn("@MetricForCount annotation on '{}' value can't be zero or negative", method.getName());
			return bean;
		}
		ProxyFactory factory = new ProxyFactory();
		factory.setTarget(bean);
		factory.addAdvice(new CatLogMetricForCountAdvice(catLogMetricForCount));
		return factory.getProxy();
	}
}
