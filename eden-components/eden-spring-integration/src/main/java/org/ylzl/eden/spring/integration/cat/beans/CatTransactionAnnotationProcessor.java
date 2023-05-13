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

import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.ylzl.eden.spring.integration.cat.CatTransaction;
import org.ylzl.eden.spring.integration.cat.aop.CatTransactionMethodInterceptor;

/**
 * Transaction 注解处理器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class CatTransactionAnnotationProcessor implements BeanPostProcessor, PriorityOrdered {

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		Class<?> targetClass = AopProxyUtils.ultimateTargetClass(bean);
		if (targetClass.isAnnotationPresent(CatTransaction.class)) {
			ProxyFactory factory = new ProxyFactory();
			factory.setTarget(bean);
			factory.addAdvice(new CatTransactionMethodInterceptor());
			return factory.getProxy();
		}
		return bean;
	}

	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE;
	}
}
