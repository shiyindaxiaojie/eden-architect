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

package org.ylzl.eden.data.auditor.masker;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.ylzl.eden.data.auditor.DataMasker;
import org.ylzl.eden.extension.ExtensionLoader;
import org.ylzl.eden.spring.framework.error.util.AssertUtils;

import java.util.Map;
import java.util.Set;

/**
 * 数据脱敏管理器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Slf4j
public class DataMaskerManager {

	private static final Map<String, DataMasker> CACHE = Maps.newConcurrentMap();

	private static final Set<String> INTERNAL_KEYS = Sets.newConcurrentHashSet();

	private static boolean allowExtensionOverriding = false;

	public static void loadExtensions() {
		ExtensionLoader<DataMasker> extensionLoader = ExtensionLoader.getExtensionLoader(DataMasker.class);
		Set<String> extensions = extensionLoader.getSupportedExtensions();
		for (String extension : extensions) {
			DataMasker dataMasker = extensionLoader.getExtension(extension);
			CACHE.put(extension, dataMasker);
			INTERNAL_KEYS.add(extension);
		}
	}

	public static void addDataMasker(String name, DataMasker dataMasker) {
		if (INTERNAL_KEYS.contains(name) && !allowExtensionOverriding) {
			return;
		}
		CACHE.put(name, dataMasker);
	}

	public static DataMasker getDataMasker(String name) {
		AssertUtils.isTrue(CACHE.containsKey(name), "EXT-LOAD-404", name);
		return CACHE.get(name);
	}

	public static void setAllowExtensionOverriding(boolean allowExtensionOverriding) {
		DataMaskerManager.allowExtensionOverriding = allowExtensionOverriding;
	}
}
