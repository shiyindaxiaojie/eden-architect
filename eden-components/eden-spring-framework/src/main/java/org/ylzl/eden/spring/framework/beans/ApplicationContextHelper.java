/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ylzl.eden.spring.framework.beans;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.*;
import org.springframework.context.*;
import org.springframework.core.ResolvableType;
import org.ylzl.eden.commons.lang.ArrayUtils;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Map;

/**
 * Spring 依赖注册/查找工具
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class ApplicationContextHelper implements ApplicationContextAware, BeanFactoryPostProcessor {

	private static final String SPRING_APPLICATION_NAME = "spring.application.name";

	private static final BeanNameGenerator beanNameGenerator = new DefaultBeanNameGenerator();
	private static ApplicationContext applicationContext;

	private static ConfigurableListableBeanFactory beanFactory;

	@Override
	public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
		ApplicationContextHelper.applicationContext = applicationContext;
	}

	@Override
	public void postProcessBeanFactory(@NotNull ConfigurableListableBeanFactory beanFactory) throws BeansException {
		ApplicationContextHelper.beanFactory = beanFactory;
	}

	/**
	 * 获取 Bean
	 *
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	@SuppressWarnings("unchecked")
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

	/**
	 * 获取 Bean
	 *
	 * @param name
	 * @param <T>
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) {
		return (T) applicationContext.getBean(name);
	}

	/**
	 * 获取 Bean
	 *
	 * @param name
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	public static <T> T getBean(String name, Class<T> clazz) {
		return applicationContext.getBean(name, clazz);
	}

	/**
	 * 获取 Bean
	 *
	 * @param clazz
	 * @param params
	 * @param <T>
	 * @return
	 */
	public static <T> T getBean(Class<T> clazz, Object... params) {
		return applicationContext.getBean(clazz, params);
	}

	/**
	 * 根据参数类型获取 Bean
	 *
	 * @param parameterizedType
	 * @param <T>
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(ParameterizedType parameterizedType) {
		final Class<T> rawType = (Class<T>) parameterizedType.getRawType();
		final Class<?>[] genericTypes = Arrays.stream(parameterizedType.getActualTypeArguments()).map(type -> (Class<?>) type).toArray(Class[]::new);
		final String[] beanNames = getBeanFactory().getBeanNamesForType(ResolvableType.forClassWithGenerics(rawType, genericTypes));
		return getBean(beanNames[0], rawType);
	}

	/**
	 * 获取类的 Bean 集合
	 *
	 * @param type
	 * @param <T>
	 * @return
	 */
	public static <T> Map<String, T> getBeansOfType(Class<T> type) {
		return getBeanFactory().getBeansOfType(type);
	}

	/**
	 * 获取实例的 Bean 名称
	 *
	 * @param type
	 * @return
	 */
	public static String[] getBeanNamesForType(Class<?> type) {
		return getBeanFactory().getBeanNamesForType(type);
	}

	/**
	 * 获取属性
	 *
	 * @return
	 */
	public static String getProperty(String key) {
		if (null == applicationContext) {
			return null;
		}
		return applicationContext.getEnvironment().getProperty(key);
	}

	/**
	 * 获取应用名
	 *
	 * @return
	 */
	public static String getApplicationName() {
		return getProperty(SPRING_APPLICATION_NAME);
	}

	/**
	 * 获取运行环境
	 *
	 * @return
	 */
	public static String[] getActiveProfiles() {
		if (null == applicationContext) {
			return null;
		}
		return applicationContext.getEnvironment().getActiveProfiles();
	}

	/**
	 * 获取运行环境
	 *
	 * @return
	 */
	public static String getActiveProfile() {
		final String[] activeProfiles = getActiveProfiles();
		return ArrayUtils.isNotEmpty(activeProfiles) ? activeProfiles[0] : null;
	}

	/**
	 * 注册 Bean
	 *
	 * @param beanName
	 * @param bean
	 * @param <T>
	 */
	public static <T> void registerBean(String beanName, T bean) {
		final ConfigurableListableBeanFactory factory = getConfigurableBeanFactory();
		factory.autowireBean(bean);
		factory.registerSingleton(beanName, bean);
	}

	/**
	 * 注册 Bean
	 *
	 * @param beanClass
	 * @param registry
	 */
	public static void registerBean(Class<?> beanClass, BeanDefinitionRegistry registry) {
		BeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(beanClass).getBeanDefinition();
		String beanName = beanNameGenerator.generateBeanName(beanDefinition, registry);
		registry.registerBeanDefinition(beanName, beanDefinition);
	}

	/**
	 * 销毁 Bean
	 *
	 * @param beanName
	 */
	public static void destroyBean(String beanName) {
		final ConfigurableListableBeanFactory factory = getConfigurableBeanFactory();
		if (!(factory instanceof DefaultSingletonBeanRegistry)) {
			throw new ApplicationContextException("Can not destroy bean, the factory is not a DefaultSingletonBeanRegistry");
		}
		DefaultSingletonBeanRegistry registry = (DefaultSingletonBeanRegistry) factory;
		registry.destroySingleton(beanName);
	}

	/**
	 * 发布事件
	 *
	 * @param event
	 */
	public static void publishEvent(ApplicationEvent event) {
		if (applicationContext != null) {
			applicationContext.publishEvent(event);
		}
	}

	/**
	 * 获取 ApplicationContext
	 *
	 * @return
	 */
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * 获取 BeanFactory
	 *
	 * @return
	 */
	public static ListableBeanFactory getBeanFactory() {
		return beanFactory == null ? applicationContext : beanFactory;
	}

	/**
	 * 获取 ConfigurableListableBeanFactory
	 *
	 * @return
	 */
	public static ConfigurableListableBeanFactory getConfigurableBeanFactory() {
		final ConfigurableListableBeanFactory factory;
		if (null != beanFactory) {
			factory = beanFactory;
		} else if (applicationContext instanceof ConfigurableApplicationContext) {
			factory = ((ConfigurableApplicationContext) applicationContext).getBeanFactory();
		} else {
			throw new ApplicationContextException("No ConfigurableListableBeanFactory from context!");
		}
		return factory;
	}
}
