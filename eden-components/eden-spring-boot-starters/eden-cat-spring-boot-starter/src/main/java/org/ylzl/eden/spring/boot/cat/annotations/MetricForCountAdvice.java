package org.ylzl.eden.spring.boot.cat.annotations;

import com.dianping.cat.Cat;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.support.annotation.AnnotationMethodMatcher;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * TODO
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@RequiredArgsConstructor
public class MetricForCountAdvice implements AfterReturningAdvice {

	private final MetricForCount metricForCount;

	@Override
	public void afterReturning(Object returnValue, @NotNull Method method, @NotNull Object[] args, Object target) {
		AnnotationMethodMatcher matcher = new AnnotationMethodMatcher(MetricForCount.class);
		if (matcher.matches(method, Objects.requireNonNull(target).getClass())) {
			Cat.logMetricForCount(metricForCount.name(), metricForCount.count());
		}
	}
}
