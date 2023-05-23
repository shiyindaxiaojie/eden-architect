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

package org.ylzl.eden.extension.strategy;

import org.ylzl.eden.commons.lang.ArrayUtils;

import java.util.Arrays;
import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.StreamSupport;

/**
 * LoadingStrategy 容器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @see ServiceLoader
 * @since 2.4.13
 */
public class LoadingStrategyHolder {

	/**
	 * ServiceLoader 加载策略
	 */
	public static volatile LoadingStrategy[] strategies = loadLoadingStrategies();

	/**
	 * 设置 ServiceLoader 加载策略
	 *
	 * @param strategies
	 */
	public static void setLoadingStrategies(LoadingStrategy... strategies) {
		if (ArrayUtils.isNotEmpty(strategies)) {
			LoadingStrategyHolder.strategies = strategies;
		}
	}

	/**
	 * 获取 ServiceLoader 加载策略
	 *
	 * @return
	 */
	public static LoadingStrategy[] loadLoadingStrategies() {
		return StreamSupport.stream(ServiceLoader.load(LoadingStrategy.class).spliterator(), false)
			.sorted()
			.toArray(LoadingStrategy[]::new);
	}

	/**
	 * 获取 ServiceLoader 加载策略
	 *
	 * @return
	 */
	public static List<LoadingStrategy> getLoadingStrategies() {
		return Arrays.asList(strategies);
	}
}
