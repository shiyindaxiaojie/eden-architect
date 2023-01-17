package org.ylzl.eden.spring.framework.aop.util;

import lombok.experimental.UtilityClass;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.AopProxy;

import java.lang.reflect.Field;

/**
 * AOP 工具类
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@UtilityClass
public class AopUtils {

	public static final String CGLIB_FIELD = "CGLIB$CALLBACK_0";

	public static final String JDK_FIELD = "h";

	public static final String ADVISED_FIELD = "advised";

	public static Object getDynamicProxyTargetObject(Object object) {
		if (!org.springframework.aop.support.AopUtils.isAopProxy(object)) {
			return object;
		}
		try {
			return org.springframework.aop.support.AopUtils.isJdkDynamicProxy(object)?
				getDynamicProxyTargetObjectFromJDK(object):
				getDynamicProxyTargetObjectByCglib(object);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static Object getDynamicProxyTargetObjectByCglib(Object obj) throws Exception {
		Field field = obj.getClass().getDeclaredField(CGLIB_FIELD);
		field.setAccessible(true);
		Object dynamicAdvisedInterceptor = field.get(obj);
		Field advised = dynamicAdvisedInterceptor.getClass().getDeclaredField(ADVISED_FIELD);
		advised.setAccessible(true);
		return ((AdvisedSupport) advised.get(dynamicAdvisedInterceptor)).getTargetSource().getTarget();
	}

	private static Object getDynamicProxyTargetObjectFromJDK(Object obj) throws Exception {
		Field field = obj.getClass().getSuperclass().getDeclaredField(JDK_FIELD);
		field.setAccessible(true);
		AopProxy aopProxy = (AopProxy) field.get(obj);
		Field advised = aopProxy.getClass().getDeclaredField(ADVISED_FIELD);
		advised.setAccessible(true);
		return ((AdvisedSupport) advised.get(aopProxy)).getTargetSource().getTarget();
	}

}
