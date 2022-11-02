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

package org.ylzl.eden.swagger2.spring.boot.env;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Swagger2 配置属性
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "swagger2")
public class Swagger2Properties {

	private boolean enabled = true;

	private String contactEmail = "1813986321@qq.com";

	private String contactName = "梦想歌";

	private String contactUrl = null;

	private String defaultIncludePattern = "/api/.*";

	private String description = "API documentation";

	private String host = null;

	private String license = null;

	private String licenseUrl = null;

	private String[] protocols = {};

	private String termsOfServiceUrl = null;

	private String title = "Application API";

	private boolean useDefaultResponseMessages = false;

	private String version = "1.0.0";
}
