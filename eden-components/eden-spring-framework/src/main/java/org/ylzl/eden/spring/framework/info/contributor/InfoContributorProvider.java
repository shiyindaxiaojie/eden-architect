/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ylzl.eden.spring.framework.info.contributor;

import lombok.Data;
import org.springframework.boot.info.BuildProperties;
import org.ylzl.eden.commons.lang.StringConstants;

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
		StringBuilder packagStr = new StringBuilder();
		packagStr
			.append(buildProperties.getGroup())
			.append(StringConstants.DOT)
			.append(
				buildProperties.getArtifact().replaceAll(StringConstants.MINUS, StringConstants.DOT))
			.append(StringConstants.DOT)
			.append(suffix);
		return packagStr.toString();
	}
}
