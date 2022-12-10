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

package org.ylzl.eden.spring.boot.info.contributor;

import lombok.Data;
import org.springframework.boot.info.BuildProperties;
import org.ylzl.eden.commons.lang.Strings;

/**
 * 应用构建信息类
 *
 * <p>获取 Spring Boot Maven 插件生成的 build-info.properties
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Data
public class InfoContributorProvider {

	private final BuildProperties buildProperties;

	public InfoContributorProvider(BuildProperties buildProperties) {
		this.buildProperties = buildProperties;
	}

	public String resolvePackage(String suffix) {
		return buildProperties.getGroup() +
			Strings.DOT +
			buildProperties.getArtifact().replaceAll(Strings.MINUS, Strings.DOT) +
			Strings.DOT +
			suffix;
	}
}
