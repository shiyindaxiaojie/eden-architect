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

package org.ylzl.eden.swagger2.spring.boot.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementServerProperties;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Role;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.servlet.DispatcherServlet;
import org.ylzl.eden.commons.lang.StringUtils;
import org.ylzl.eden.commons.lang.Strings;
import org.ylzl.eden.spring.framework.bootstrap.constant.SpringProperties;
import org.ylzl.eden.spring.integration.swagger2.customizer.Swagger2Customizer;
import org.ylzl.eden.swagger2.spring.boot.env.DefaultSwagger2Customizer;
import org.ylzl.eden.swagger2.spring.boot.env.Swagger2Properties;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.schema.AlternateTypeRule;
import springfox.documentation.service.ApiInfo;
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
 * Swagger 自动装配
 *
 * <p>从 Spring Boot 1.5.x 升级到 2.4.x</p>
 *
 * <ul>
 *   <li>org.springframework.boot.actuate.autoconfigure.ManagementServerProperties 迁移到 {@link
 *       ManagementServerProperties}
 *   <li>{@code managementServerProperties.getServlet().getContextPath()} 修改为 {@code
 *       managementServerProperties.getServlet().getContextPath()}
 * </ul>
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ConditionalOnClass({
	ApiInfo.class,
	BeanValidatorPluginsConfiguration.class,
	Servlet.class,
	DispatcherServlet.class
})
@ConditionalOnProperty(value = "swagger2.enabled", matchIfMissing = true)
@ConditionalOnWebApplication
@EnableConfigurationProperties(Swagger2Properties.class)
@EnableSwagger2
@Import(BeanValidatorPluginsConfiguration.class)
@Slf4j
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Configuration(proxyBeanMethods = false)
public class Swagger2AutoConfiguration {

	public static final String DEFAULT_GROUP_NAME = "management";

	private static final String MSG_AUTOWIRED_SWAGGER = "Autowired Swagger";

	private static final String MSG_STARTD_SWAGGER = "Startded Swagger in {} ms";

	private final Swagger2Properties properties;

	private final ManagementServerProperties managementServerProperties;

	@Value(SpringProperties.NAME_PATTERN)
	private String applicationName;

	public Swagger2AutoConfiguration(
		Swagger2Properties properties, ManagementServerProperties managementServerProperties) {
		this.properties = properties;
		this.managementServerProperties = managementServerProperties;
	}

	@ConditionalOnMissingBean(name = "swaggerSpringfoxApiDocket")
	@Bean
	public Docket swaggerSpringfoxApiDocket(
		List<Swagger2Customizer> swagger2Customizers,
		ObjectProvider<AlternateTypeRule[]> alternateTypeRules) {
		log.debug(MSG_AUTOWIRED_SWAGGER);
		StopWatch watch = new StopWatch();
		watch.start();
		Docket docket = createDocket();
		for (Swagger2Customizer customizer : swagger2Customizers) {
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
	public DefaultSwagger2Customizer swaggerCustomizer() {
		return new DefaultSwagger2Customizer(properties);
	}

	@Bean
	@ConditionalOnClass(ManagementServerProperties.class)
	@ConditionalOnProperty("management.endpoints.web.base-path")
	@ConditionalOnExpression("'${management.endpoints.web.base-path}'.length() > 0")
	@ConditionalOnMissingBean(name = "swaggerSpringfoxManagementDocket")
	public Docket swaggerSpringfoxManagementDocket() {

		ApiInfo apiInfo =
			new ApiInfo(
				StringUtils.capitalize(applicationName),
				Strings.EMPTY,
				properties.getVersion(),
				Strings.EMPTY,
				ApiInfo.DEFAULT_CONTACT,
				Strings.EMPTY,
				Strings.EMPTY,
				new ArrayList<>());

		return createDocket()
			.apiInfo(apiInfo)
			.useDefaultResponseMessages(properties.isUseDefaultResponseMessages())
			.groupName(DEFAULT_GROUP_NAME)
			.host(properties.getHost())
			.protocols(new HashSet<>(Arrays.asList(properties.getProtocols())))
			.forCodeGeneration(true)
			.directModelSubstitute(ByteBuffer.class, String.class)
			.genericModelSubstitutes(ResponseEntity.class)
			.select()
			.paths(
				PathSelectors.regex(
					StringUtils.join(managementServerProperties.getBasePath(), ".*")))
			.build();
	}

	protected Docket createDocket() {
		return new Docket(DocumentationType.SWAGGER_2);
	}
}
