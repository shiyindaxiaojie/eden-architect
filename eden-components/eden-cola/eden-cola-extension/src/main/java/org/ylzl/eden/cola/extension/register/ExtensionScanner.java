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

package org.ylzl.eden.cola.extension.register;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.ylzl.eden.cola.extension.Extension;
import org.ylzl.eden.cola.extension.Extensions;

import java.util.Map;

/**
 * 扩展点扫描
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@RequiredArgsConstructor
@Slf4j
public class ExtensionScanner implements ApplicationContextAware, InitializingBean {

	private ApplicationContext applicationContext;

	private final ExtensionRegister extensionRegister;

	@Override
	public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Map<String, Object> extensionBeans = applicationContext.getBeansWithAnnotation(Extension.class);
		extensionBeans.values().forEach(extensionRegister::registerExtension);

		Map<String, Object> extensionsBeans = applicationContext.getBeansWithAnnotation(Extensions.class);
		extensionsBeans.values().forEach(extensionRegister::registerExtension);
	}
}
