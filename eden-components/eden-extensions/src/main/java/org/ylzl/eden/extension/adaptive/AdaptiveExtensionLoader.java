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

package org.ylzl.eden.extension.adaptive;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ylzl.eden.commons.lang.ClassLoaderUtils;
import org.ylzl.eden.extension.ExtensionLoader;
import org.ylzl.eden.extension.util.Holder;

/**
 * 基于 @Adaptive 的扩展点加载器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
@Slf4j
public class AdaptiveExtensionLoader<T> {

	/**
	 * 缓存自适应扩展点实例
	 */
	private final Holder<Object> cachedAdaptiveInstance = new Holder<>();

	private volatile Class<?> cachedAdaptiveClass = null;

	private volatile Throwable createAdaptiveInstanceError;

	private final ExtensionLoader<T> extensionLoader;

	/**
	 * 获取 @Adaptive 扩展点
	 *
	 * @return
	 */
	public T getAdaptiveExtension() {
		Object instance = cachedAdaptiveInstance.get();
		if (instance == null) {
			if (createAdaptiveInstanceError != null) {
				throw new IllegalStateException("Failed to create adaptive instance: " +
					createAdaptiveInstanceError.toString(),
					createAdaptiveInstanceError);
			}

			synchronized (cachedAdaptiveInstance) {
				instance = cachedAdaptiveInstance.get();
				if (instance == null) {
					try {
						instance = createAdaptiveExtension();
						cachedAdaptiveInstance.set(instance);
					} catch (Throwable t) {
						createAdaptiveInstanceError = t;
						throw new IllegalStateException("Failed to create adaptive instance: " + t.toString(), t);
					}
				}
			}
		}

		return (T) instance;
	}

	/**
	 * 缓存 @Adaptive 扩展类
	 *
	 * @param clazz
	 * @param overridden
	 */
	public void cacheAdaptiveClass(Class<?> clazz, boolean overridden) {
		if (cachedAdaptiveClass == null || overridden) {
			cachedAdaptiveClass = clazz;
		} else if (!cachedAdaptiveClass.equals(clazz)) {
			throw new IllegalStateException("More than 1 adaptive class found: "
				+ cachedAdaptiveClass.getName()
				+ ", " + clazz.getName());
		}
	}

	public Object getLoadedAdaptiveExtensionInstances() {
		return cachedAdaptiveInstance.get();
	}

	/**
	 * 创建 @Adaptive 扩展点
	 *
	 * @return
	 */
	private T createAdaptiveExtension() {
		try {
			return extensionLoader.injectExtension((T) getAdaptiveExtensionClass().newInstance());
		} catch (Exception e) {
			throw new IllegalStateException("Can't create adaptive extension " + extensionLoader.getType() +
				", cause: " + e.getMessage(), e);
		}
	}

	/**
	 * 获取 @Adaptive 扩展类
	 *
	 * @return
	 */
	private Class<?> getAdaptiveExtensionClass() {
		extensionLoader.getExtensionClasses();
		if (cachedAdaptiveClass != null) {
			return cachedAdaptiveClass;
		}
		return cachedAdaptiveClass = createAdaptiveExtensionClass();
	}

	/**
	 * 创建 @Adaptive 扩展类
	 *
	 * @return
	 */
	private Class<?> createAdaptiveExtensionClass() {
		String code = new AdaptiveClassCodeGenerator(extensionLoader.getType(),
			extensionLoader.getCachedDefaultName()).generate();
		ClassLoader classLoader = ClassLoaderUtils.getClassLoader(ExtensionLoader.class);
		org.ylzl.eden.extension.compile.Compiler compiler =
			ExtensionLoader.getExtensionLoader(org.ylzl.eden.extension.compile.Compiler.class)
				.getAdaptiveExtension();
		return compiler.compile(code, classLoader);
	}
}
