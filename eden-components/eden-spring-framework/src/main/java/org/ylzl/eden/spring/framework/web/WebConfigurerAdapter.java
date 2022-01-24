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

package org.ylzl.eden.spring.framework.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.server.MimeMappings;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.core.env.Environment;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.ylzl.eden.spring.framework.core.env.SpringFrameworkProperties;

import javax.servlet.ServletContext;
import java.util.concurrent.TimeUnit;

/**
 * Web 配置适配器
 *
 * <p>从 Spring Boot 1.X 升级到 2.X
 *
 * <ul>
 *   <li>org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer 变更为 {@link
 *       WebServerFactoryCustomizer}
 *   <li>org.springframework.boot.context.embedded.MimeMappings 迁移到 {@link MimeMappings}
 *   <li>org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory 迁移到
 *       {@link JettyServletWebServerFactory}
 *   <li>org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory 迁移到
 *       {@link TomcatServletWebServerFactory}
 *   <li>org.springframework.boot.context.embedded.undertow.UndertowEmbeddedServletContainerFactory
 *       迁移到 {@link UndertowServletWebServerFactory}
 * </ul>
 *
 * @author gyl
 * @since 2.0.0
 */
@EnableRestErrorAdvice
@Slf4j
public class WebConfigurerAdapter implements WebMvcConfigurer, ServletContextInitializer {

	protected static final String[] RESOURCE_LOCATIONS = {
		"classpath:/static/app/", "classpath:/static/content/", "classpath:/static/i18n/"
	};
	protected static final String[] RESOURCE_PATHS = {"/app/*", "/content/*", "/i18n/*"};

	private final SpringFrameworkProperties properties;

	private final Environment env;

	public WebConfigurerAdapter(SpringFrameworkProperties properties, Environment env) {
		this.properties = properties;
		this.env = env;
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		ResourceHandlerRegistration resourceHandlerRegistration =
			registry.addResourceHandler(RESOURCE_PATHS);
		resourceHandlerRegistration
			.addResourceLocations(RESOURCE_LOCATIONS)
			.setCacheControl(getCacheControl());
	}

	@Override
	public void onStartup(ServletContext servletContext) {
		// TODO
	}

	protected CacheControl getCacheControl() {
		return CacheControl.maxAge(properties.getHttp().getCache().getTimeToLiveInDays(), TimeUnit.DAYS)
			.cachePublic();
	}
}
