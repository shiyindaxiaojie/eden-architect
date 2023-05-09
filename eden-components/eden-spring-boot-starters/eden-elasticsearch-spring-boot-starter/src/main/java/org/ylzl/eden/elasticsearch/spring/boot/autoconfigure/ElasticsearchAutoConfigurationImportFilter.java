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

package org.ylzl.eden.elasticsearch.spring.boot.autoconfigure;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.AutoConfigurationImportFilter;
import org.springframework.boot.autoconfigure.AutoConfigurationMetadata;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.ylzl.eden.spring.boot.bootstrap.constant.Conditions;

import java.util.Arrays;

/**
 * Elasticsearch 自动装配过滤
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class ElasticsearchAutoConfigurationImportFilter implements AutoConfigurationImportFilter, EnvironmentAware {

	private static final String MATCH_KEY = "spring.elasticsearch.enabled";

	private static final String[] IGNORE_CLASSES = {
		"org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientAutoConfiguration",
		"org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration",
		"org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchRepositoriesAutoConfiguration",
		"org.springframework.boot.autoconfigure.data.elasticsearch.ReactiveElasticsearchRepositoriesAutoConfiguration",
		"org.springframework.boot.autoconfigure.data.elasticsearch.ReactiveElasticsearchRestClientAutoConfiguration",
		"org.springframework.boot.actuate.autoconfigure.elasticsearch.ElasticsearchHealthIndicatorAutoConfiguration"
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
