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

package org.ylzl.eden.shardingsphere.spring.boot.autoconfigure;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.AutoConfigurationImportFilter;
import org.springframework.boot.autoconfigure.AutoConfigurationMetadata;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.ylzl.eden.spring.boot.bootstrap.constant.Conditions;

import java.util.Arrays;

/**
 * ShardingSphere 自动装配过滤
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class ShardingSphereAutoConfigurationImportFilter implements AutoConfigurationImportFilter, EnvironmentAware {

	private static final String MATCH_KEY = "spring.shardingsphere.enabled";

	/**
	 * ShardingSphere 5.3.0+ 已移除 Spring Boot Starter 支持
	 * 这些类在 5.4.x 中不再存在，保留此列表以便向后兼容
	 * 如果用户意外引入旧版依赖，可以正确过滤
	 */
	private static final String[] IGNORE_CLASSES = {
		// 旧版 ShardingSphere Spring Boot Starter 配置类（5.2.x 及更早版本）
		// 这些类在 5.3.0+ 中已被移除
	};

	private Environment environment;

	@Override
	public void setEnvironment(@NotNull Environment environment) {
		this.environment = environment;
	}

	@Override
	public boolean[] match(String[] autoConfigurationClasses, AutoConfigurationMetadata autoConfigurationMetadata) {
		boolean disabled = !Boolean.parseBoolean(environment.getProperty(MATCH_KEY, Conditions.TRUE));
		boolean[] match = new boolean[autoConfigurationClasses.length];
		for (int i = 0; i < autoConfigurationClasses.length; i++) {
			int index = i;
			match[i] = !disabled || Arrays.stream(IGNORE_CLASSES).noneMatch(e -> e.equals(autoConfigurationClasses[index]));
		}
		return match;
	}
}
