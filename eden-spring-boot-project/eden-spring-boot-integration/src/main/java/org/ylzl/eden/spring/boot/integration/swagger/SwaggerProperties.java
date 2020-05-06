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

package org.ylzl.eden.spring.boot.integration.swagger;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.ylzl.eden.spring.boot.integration.core.IntegrationConstants;

/**
 * Swagger 配置属性
 *
 * @author gyl
 * @since 0.0.1
 */
@Getter
@Setter
@ConfigurationProperties(prefix = IntegrationConstants.PROP_PREFIX + ".swagger")
public class SwaggerProperties {

  private Boolean enabled = true;

  private String contactEmail = SwaggerDefaults.contactEmail;

  private String contactName = SwaggerDefaults.contactName;

  private String contactUrl = SwaggerDefaults.contactUrl;

  private String defaultIncludePattern = SwaggerDefaults.defaultIncludePattern;

  private String description = SwaggerDefaults.description;

  private String host = SwaggerDefaults.host;

  private String license = SwaggerDefaults.license;

  private String licenseUrl = SwaggerDefaults.licenseUrl;

  private String[] protocols = SwaggerDefaults.protocols;

  private String termsOfServiceUrl = SwaggerDefaults.termsOfServiceUrl;

  private String title = SwaggerDefaults.title;

  private Boolean useDefaultResponseMessages = SwaggerDefaults.useDefaultResponseMessages;

  private String version = SwaggerDefaults.version;
}
