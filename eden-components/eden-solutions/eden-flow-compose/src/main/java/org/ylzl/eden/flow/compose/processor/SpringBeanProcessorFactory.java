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

package org.ylzl.eden.flow.compose.processor;

import org.ylzl.eden.flow.compose.Processor;
import org.ylzl.eden.spring.framework.beans.ApplicationContextHelper;

/**
 * 基于 Spring Bean 机制实例化流程工厂
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class SpringBeanProcessorFactory implements ProcessorFactory {

	/**
	 * 实例化
	 *
	 * @param className   类名
	 * @param processName 名称
	 * @return
	 * @throws Exception
	 */
	@Override
	public Processor newInstance(String className, String processName) throws Exception {
		Class<?> clazz = Class.forName(className);
		Object bean = ApplicationContextHelper.getBean(clazz);
		if (bean == null) {
			throw new IllegalArgumentException("Spring Bean '" + className + "' is not defined");
		}

		if (!(bean instanceof Processor)) {
			throw new IllegalArgumentException("Spring Bean '" + className + "' is not instance of Processor");
		}

		Processor processor = (Processor) bean;
		processor.setName(processName);
		return processor;
	}
}
