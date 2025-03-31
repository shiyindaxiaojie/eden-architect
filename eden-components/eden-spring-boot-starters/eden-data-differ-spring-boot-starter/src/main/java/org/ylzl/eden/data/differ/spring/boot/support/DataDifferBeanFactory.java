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

package org.ylzl.eden.data.differ.spring.boot.support;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ylzl.eden.data.differ.DataDiffer;
import org.ylzl.eden.spring.framework.beans.ApplicationContextHelper;

import java.util.Map;

/**
 * 数据比对操作实例工厂类
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
@Slf4j
public class DataDifferBeanFactory {

	private static final String DATA_DIFFER_TYPE_NOT_FOUND = "Data differ type named '{}' not found";

	private final String primary;

	public DataDiffer getBean() {
		return getBean(primary);
	}

	public DataDiffer getBean(String type) {
		Map<String, DataDiffer> dataDiffers = ApplicationContextHelper.getBeansOfType(DataDiffer.class);
		return dataDiffers.values().stream()
			.filter(predicate -> predicate.getType().equalsIgnoreCase(type))
			.findFirst()
			.orElseThrow(() -> new RuntimeException(DATA_DIFFER_TYPE_NOT_FOUND));
	}
}
