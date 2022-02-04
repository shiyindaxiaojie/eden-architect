package org.ylzl.eden.spring.framework.beans;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Spring 依赖查找工具
 *
 * @author gyl
 * @since 2.4.x
 */
public class ApplicationContextHelper implements ApplicationContextAware {

	private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		ApplicationContextHelper.applicationContext = applicationContext;
	}

	public static <T> T getBean(Class<T> clazz) {
		T beanInstance = null;
		try {
			beanInstance = applicationContext.getBean(clazz);
		} catch (Exception ignored) {
		}

		if (beanInstance == null) {
			String simpleName = clazz.getSimpleName();
			String beanName = Character.toLowerCase(simpleName.charAt(0)) + simpleName.substring(1);
			beanInstance = (T) applicationContext.getBean(beanName);
		}
		return beanInstance;
	}

	public static Object getBean(String clazz) {
		return applicationContext.getBean(clazz);
	}

	public static <T> T getBean(String name, Class<T> clazz) {
		return applicationContext.getBean(name, clazz);
	}

	public static <T> T getBean(Class<T> clazz, Object... params) {
		return applicationContext.getBean(clazz, params);
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}
}
