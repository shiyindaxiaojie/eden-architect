package org.ylzl.eden.spring.boot.logging;

import lombok.RequiredArgsConstructor;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.util.PathMatcher;
import org.ylzl.eden.commons.lang.reflect.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 日志切面检查点
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
public class LoggingAspectPointcut extends StaticMethodMatcherPointcut {

	private final PathMatcher pathMatcher;

	private final String[] packages;

	/**
	 * 匹配切点
	 *
	 * @param method 调用方法
	 * @param targetClass 目标类
	 * @return 是否匹配
	 */
	@Override
	public boolean matches(Method method, Class<?> targetClass) {
		if (!ReflectionUtils.isPublic(method)) {
			return false;
		}

		return Arrays.stream(packages).anyMatch(pkg -> pathMatcher.match(pkg, targetClass.getName()));
	}
}
