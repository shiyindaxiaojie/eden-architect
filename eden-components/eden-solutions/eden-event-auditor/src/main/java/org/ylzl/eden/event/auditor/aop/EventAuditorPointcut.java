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

	private boolean matchesEventAuditor(Method method) {
		return !AnnotatedElementUtils.findAllMergedAnnotations(method, EventAuditor.class).isEmpty();
	}

	private boolean matchesEventAuditors(Method method) {
		return !AnnotatedElementUtils.findAllMergedAnnotations(method, EventAuditors.class).isEmpty();
	}
}
