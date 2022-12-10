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

package org.ylzl.eden.cola.extension.executor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ylzl.eden.cola.extension.BizScenario;
import org.ylzl.eden.cola.extension.exception.ExtensionException;
import org.ylzl.eden.cola.extension.register.ExtensionRegister;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 扩展点执行器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
@Slf4j
public class ExtensionExecutor {

	private final ExtensionRegister extensionRegister;

	public <T, R> R execute(Class<T> extensionClass, BizScenario bizScenario, Function<T, R> function) {
		T extension = locateExtension(extensionClass, bizScenario);
		return function.apply(extension);
	}

	public <T> void executeVoid(Class<T> extensionClass, BizScenario bizScenario, Consumer<T> consumer) {
		T extension = locateExtension(extensionClass, bizScenario);
		consumer.accept(extension);
	}

	public <Ext> Ext locateExtension(Class<Ext> extensionClass, BizScenario bizScenario) {
		Ext extension = matchUniqueIdentity(extensionClass, bizScenario);
		if (extension != null) {
			return extension;
		}

		extension = matchDefaultScenario(extensionClass, bizScenario);
		if (extension != null) {
			return extension;
		}

		extension = matchDefaultUseCase(extensionClass, bizScenario);
		if (extension != null) {
			return extension;
		}

		throw new ExtensionException("Can not find extension with ExtensionPoint: " +
			extensionClass + ", BizScenario:" + bizScenario.getUniqueIdentity());
	}

	private <Ext> Ext matchUniqueIdentity(Class<Ext> extensionClass, BizScenario bizScenario) {
		log.debug("Match unique identity: {}", bizScenario.getUniqueIdentity());
		return locate(extensionClass, bizScenario.getUniqueIdentity());
	}

	private <Ext> Ext matchDefaultScenario(Class<Ext> extensionClass, BizScenario bizScenario) {
		log.debug("Match default scenario: {}", bizScenario.getIdentityWithDefaultScenario());
		return locate(extensionClass, bizScenario.getIdentityWithDefaultScenario());
	}

	private <Ext> Ext matchDefaultUseCase(Class<Ext> extensionClass, BizScenario bizScenario) {
		log.debug("Match default use case: {}", bizScenario.getIdentityWithDefaultUseCase());
		return locate(extensionClass, bizScenario.getIdentityWithDefaultUseCase());
	}

	private <Ext> Ext locate(Class<Ext> extensionClass, String uniqueIdentity) {
		return extensionRegister.getExtension(extensionClass, uniqueIdentity);
	}
}
