/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ylzl.eden.spring.integration.cat.beans;

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
import org.ylzl.eden.spring.integration.cat.aop.CatLogMetricForCountAdvice;
import org.ylzl.eden.spring.integration.cat.core.CatLogMetricForCount;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Cat.logMetricForCount 注解处理器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
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
