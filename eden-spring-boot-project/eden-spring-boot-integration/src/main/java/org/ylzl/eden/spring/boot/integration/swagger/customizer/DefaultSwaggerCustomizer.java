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

package org.ylzl.eden.spring.boot.integration.swagger.customizer;

import org.springframework.core.Ordered;
import org.springframework.http.ResponseEntity;
import org.ylzl.eden.spring.boot.integration.swagger.SwaggerProperties;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spring.web.plugins.Docket;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import static springfox.documentation.builders.PathSelectors.regex;

/**
 * 默认的 Swagger 自定义实现
 *
 * @author gyl
 * @since 0.0.1
 */
public class DefaultSwaggerCustomizer implements SwaggerCustomizer, Ordered {

  public static final int DEFAULT_ORDER = 0;

  private int order = DEFAULT_ORDER;

  private final SwaggerProperties properties;

  public DefaultSwaggerCustomizer(SwaggerProperties properties) {
    this.properties = properties;
  }

  @Override
  public void customize(Docket docket) {
    Contact contact =
        new Contact(
            properties.getContactName(), properties.getContactUrl(), properties.getContactEmail());

    ApiInfo apiInfo =
        new ApiInfo(
            properties.getTitle(),
            properties.getDescription(),
            properties.getVersion(),
            properties.getTermsOfServiceUrl(),
            contact,
            properties.getLicense(),
            properties.getLicenseUrl(),
            new ArrayList<VendorExtension>());

    docket
        .host(properties.getHost())
        .protocols(new HashSet<>(Arrays.asList(properties.getProtocols())))
        .apiInfo(apiInfo)
        .useDefaultResponseMessages(properties.getUseDefaultResponseMessages())
        .forCodeGeneration(true)
        .directModelSubstitute(ByteBuffer.class, String.class)
        .genericModelSubstitutes(ResponseEntity.class)
        .select()
        .paths(regex(properties.getDefaultIncludePattern()))
        .build();
  }

  public void setOrder(int order) {
    this.order = order;
  }

  @Override
  public int getOrder() {
    return order;
  }
}
