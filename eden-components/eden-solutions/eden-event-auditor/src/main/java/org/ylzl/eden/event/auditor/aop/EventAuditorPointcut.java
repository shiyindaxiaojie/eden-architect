package org.ylzl.eden.event.auditor.aop;

import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.ClassUtils;
import org.ylzl.eden.commons.lang.reflect.ReflectionUtils;
import org.ylzl.eden.event.auditor.EventAuditor;
import org.ylzl.eden.event.auditor.EventAuditors;

import java.lang.reflect.Method;

/**
 * 事件审计切点
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class EventAuditorPointcut extends StaticMethodMatcherPointcut {

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
		return matchesEventAuditor(method) ||
			matchesEventAuditor(specificMethod) ||
			matchesEventAuditors(method) ||
			matchesEventAuditors(specificMethod);
	}

	/**
	 * 判断是否匹配 {@code EventAuditor} 注解
	 *
	 * @param method 调用方法
	 * @return 是否匹配
	 */
	private boolean matchesEventAuditor(Method method) {
		return !AnnotatedElementUtils.findAllMergedAnnotations(method, EventAuditor.class).isEmpty();
	}

	/**
	 * 判断是否匹配 {@code EventAuditors} 注解
	 *
	 * @param method 调用方法
	 * @return 是否匹配
	 */
	private boolean matchesEventAuditors(Method method) {
		return !AnnotatedElementUtils.findAllMergedAnnotations(method, EventAuditors.class).isEmpty();
	}
}
