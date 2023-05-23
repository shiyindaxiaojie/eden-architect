package org.ylzl.eden.spring.integration.cat.aop;

import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.ClassUtils;
import org.ylzl.eden.commons.lang.reflect.ReflectionUtils;
import org.ylzl.eden.spring.integration.cat.CatMetric;
import org.ylzl.eden.spring.integration.cat.CatMetrics;

import java.lang.reflect.Method;

/**
 * CatLogMetricForCount 切点
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class CatMetricPointcut extends StaticMethodMatcherPointcut {

	/**
	 * 匹配切点
	 *
	 * @param method      调用方法
	 * @param targetClass 目标类
	 * @return 是否匹配
	 */
	@Override
	public boolean matches(Method method, Class<?> targetClass) {
		if (!ReflectionUtils.isPublic(method)) {
			return false;
		}

		Method specificMethod = ClassUtils.getMostSpecificMethod(method, targetClass);
		specificMethod = BridgeMethodResolver.findBridgedMethod(specificMethod);
		return matchesCatLogMetricForCount(method) ||
			matchesCatLogMetricForCount(specificMethod) ||
			matchesCatLogMetricForCounts(method) ||
			matchesCatLogMetricForCounts(specificMethod);
	}

	/**
	 * 判断是否匹配 {@code CatLogMetricForCount} 注解
	 *
	 * @param method 调用方法
	 * @return 是否匹配
	 */
	private boolean matchesCatLogMetricForCount(Method method) {
		return !AnnotatedElementUtils.findAllMergedAnnotations(method, CatMetric.class).isEmpty();
	}

	/**
	 * 判断是否匹配 {@code CatLogMetricForCounts} 注解
	 *
	 * @param method 调用方法
	 * @return 是否匹配
	 */
	private boolean matchesCatLogMetricForCounts(Method method) {
		return !AnnotatedElementUtils.findAllMergedAnnotations(method, CatMetrics.class).isEmpty();
	}
}
