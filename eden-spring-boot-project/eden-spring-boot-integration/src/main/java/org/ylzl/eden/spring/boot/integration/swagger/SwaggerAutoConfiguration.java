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

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.ManagementServerProperties;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.servlet.DispatcherServlet;
import org.ylzl.eden.spring.boot.commons.lang.StringConstants;
import org.ylzl.eden.spring.boot.commons.lang.StringUtils;
import org.ylzl.eden.spring.boot.framework.core.FrameworkConstants;
import org.ylzl.eden.spring.boot.integration.core.IntegrationConstants;
import org.ylzl.eden.spring.boot.integration.swagger.customizer.DefaultSwaggerCustomizer;
import org.ylzl.eden.spring.boot.integration.swagger.customizer.SwaggerCustomizer;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.schema.AlternateTypeRule;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.Servlet;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Swagger 自动配置
 *
 * @author gyl
 * @since 0.0.1
 */
@ConditionalOnClass({
  ApiInfo.class,
  BeanValidatorPluginsConfiguration.class,
  Servlet.class,
  DispatcherServlet.class
})
@ConditionalOnExpression(SwaggerAutoConfiguration.EXPS_SWAGGER_ENABLED)
@ConditionalOnWebApplication
@EnableConfigurationProperties(SwaggerProperties.class)
@EnableSwagger2
@Import(BeanValidatorPluginsConfiguration.class)
@Slf4j
@Configuration
public class SwaggerAutoConfiguration {

  public static final String EXPS_SWAGGER_ENABLED =
      "${" + IntegrationConstants.PROP_PREFIX + ".swagger.enabled:true}";

  public static final String DEFAULT_GROUP_NAME = "management";

  private static final String MSG_INJECT_SWAGGER = "Autowired Swagger";

  private static final String MSG_STARTD_SWAGGER = "Startded Swagger in {} ms";

  @Value(FrameworkConstants.NAME_PATTERN)
  private String applicationName;

  private final SwaggerProperties properties;

  private final ManagementServerProperties managementServerProperties;

  public SwaggerAutoConfiguration(
      SwaggerProperties properties, ManagementServerProperties managementServerProperties) {
    this.properties = properties;
    this.managementServerProperties = managementServerProperties;
  }

  @ConditionalOnMissingBean(name = "swaggerSpringfoxApiDocket")
  @Bean
  public Docket swaggerSpringfoxApiDocket(
      List<SwaggerCustomizer> swaggerCustomizers,
      ObjectProvider<AlternateTypeRule[]> alternateTypeRules) {
    log.debug(MSG_INJECT_SWAGGER);
    StopWatch watch = new StopWatch();
    watch.start();
    Docket docket = createDocket();
    for (SwaggerCustomizer customizer : swaggerCustomizers) {
      customizer.customize(docket);
    }
    if (alternateTypeRules.getIfAvailable() != null) {
      docket.alternateTypeRules();
    }
    watch.stop();
    log.debug(MSG_STARTD_SWAGGER, watch.getTotalTimeMillis());
    return docket;
  }

  @Bean
  public DefaultSwaggerCustomizer swaggerCustomizer() {
    return new DefaultSwaggerCustomizer(properties);
  }

  @Bean
  @ConditionalOnClass(ManagementServerProperties.class)
  @ConditionalOnProperty("management.context-path")
  @ConditionalOnExpression("'${management.context-path}'.length() > 0")
  @ConditionalOnMissingBean(name = "swaggerSpringfoxManagementDocket")
  public Docket swaggerSpringfoxManagementDocket() {

    ApiInfo apiInfo =
        new ApiInfo(
            StringUtils.capitalize(applicationName),
            StringConstants.EMPTY,
            properties.getVersion(),
            StringConstants.EMPTY,
            ApiInfo.DEFAULT_CONTACT,
            StringConstants.EMPTY,
            StringConstants.EMPTY,
            new ArrayList<VendorExtension>());

    return createDocket()
        .apiInfo(apiInfo)
        .useDefaultResponseMessages(properties.getUseDefaultResponseMessages())
        .groupName(DEFAULT_GROUP_NAME)
        .host(properties.getHost())
        .protocols(new HashSet<>(Arrays.asList(properties.getProtocols())))
        .forCodeGeneration(true)
        .directModelSubstitute(ByteBuffer.class, String.class)
        .genericModelSubstitutes(ResponseEntity.class)
        .select()
        .paths(
            PathSelectors.regex(
                StringUtils.join(managementServerProperties.getContextPath(), ".*")))
        .build();
  }

  protected Docket createDocket() {
    return new Docket(DocumentationType.SWAGGER_2);
  }
}
