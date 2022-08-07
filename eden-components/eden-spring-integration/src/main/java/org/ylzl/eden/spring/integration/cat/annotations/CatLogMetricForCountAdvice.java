package org.ylzl.eden.spring.integration.cat.annotations;

import com.dianping.cat.Cat;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.support.annotation.AnnotationMethodMatcher;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * Cat.logMetricForCount 方法返回拦截
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@RequiredArgsConstructor
public class CatLogMetricForCountAdvice implements AfterReturningAdvice {

	private final CatLogMetricForCount catLogMetricForCount;

	@Override
	public void afterReturning(Object returnValue, @NotNull Method method, @NotNull Object[] args, Object target) {
		AnnotationMethodMatcher matcher = new AnnotationMethodMatcher(CatLogMetricForCount.class);
		if (matcher.matches(method, Objects.requireNonNull(target).getClass())) {
			Cat.logMetricForCount(catLogMetricForCount.name(), catLogMetricForCount.count());
		}
	}
}
