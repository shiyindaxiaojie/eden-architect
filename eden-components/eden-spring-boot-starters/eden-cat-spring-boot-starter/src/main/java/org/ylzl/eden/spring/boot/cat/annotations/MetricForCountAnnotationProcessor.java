package org.ylzl.eden.spring.boot.cat.annotations;

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
import org.springframework.util.CollectionUtils;
import org.ylzl.eden.commons.lang.StringUtils;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * TODO
 *
 * @author <a href="mailto:guoyuanlu@puyiwm.com">gyl</a>
 * @since 1.0.0
 */
@Slf4j
public class MetricForCountAnnotationProcessor implements BeanPostProcessor, PriorityOrdered {

	@Override
	public Object postProcessAfterInitialization(@NotNull Object bean, @NotNull String beanName) throws BeansException {
		Class<?> targetClass = AopProxyUtils.ultimateTargetClass(bean);
		Map<Method, MetricForCount> annotatedMethods = MethodIntrospector.selectMethods(targetClass,
			(MethodIntrospector.MetadataLookup<MetricForCount>) method ->
				AnnotationUtils.getAnnotation(method, MetricForCount.class));
		if (CollectionUtils.isEmpty(annotatedMethods)) {
			return bean;
		}
		for (Map.Entry<Method, MetricForCount> entry : annotatedMethods.entrySet()) {
			return processMetricForCount(bean, entry.getKey(), entry.getValue());
		}
		return bean;
	}

	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE;
	}

	private Object processMetricForCount(Object bean, Method method, MetricForCount metricForCount) {
		if (StringUtils.isEmpty(metricForCount.name())) {
			log.warn("@MetricForCount annotation on '{}' name can't be null or empty", method.getName());
			return bean;
		}
		if (metricForCount.count() <= 0) {
			log.warn("@MetricForCount annotation on '{}' value can't be zero or negative", method.getName());
			return bean;
		}
		ProxyFactory factory = new ProxyFactory();
		factory.setTarget(bean);
		factory.addAdvice(new MetricForCountAdvice(metricForCount));
		return factory.getProxy();
	}
}
