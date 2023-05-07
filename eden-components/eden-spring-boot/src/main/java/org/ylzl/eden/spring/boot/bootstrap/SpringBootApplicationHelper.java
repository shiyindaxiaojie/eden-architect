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

package org.ylzl.eden.spring.boot.bootstrap;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.ylzl.eden.commons.lang.StringUtils;
import org.ylzl.eden.commons.lang.Strings;
import org.ylzl.eden.extension.ExtensionLoader;
import org.ylzl.eden.spring.boot.bootstrap.env.EnvironmentInboundParser;
import org.ylzl.eden.spring.boot.bootstrap.env.EnvironmentOutboundParser;
import org.ylzl.eden.spring.boot.bootstrap.util.SpringBootProfileUtils;
import org.ylzl.eden.spring.framework.bootstrap.constant.SpringProperties;
import org.ylzl.eden.spring.framework.bootstrap.util.SpringProfileUtils;

import java.util.Set;

/**
 * Spring Boot 应用启动帮助支持
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Slf4j
@UtilityClass
public class SpringBootApplicationHelper {

	public static void run(Class<?> mainClass, String[] args, WebApplicationType webApplicationType) {
		setSystemProperties();
		try {
			SpringApplication app = new SpringApplicationBuilder(mainClass).web(webApplicationType).build();
			SpringBootProfileUtils.addDefaultProfile(app);
			app.setBannerMode(Banner.Mode.OFF);

			ConfigurableApplicationContext context = app.run(args);
			Environment env = context.getEnvironment();
			logAfterRunning(env);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	private static void setSystemProperties() {
		// Fixed elasticsearch error: availableProcessors is already set to [], rejecting []
		System.setProperty("es.set.netty.runtime.available.processors", "false");

		// Fixed dubbo error: No such any registry to refer service in consumer xxx.xxx.xxx.xxx use dubbo version 2.x.x
		System.setProperty("java.net.preferIPv4Stack", "true");

		// Fixed zookeeper error: java.io.IOException: Unreasonable length = 1048575
		System.setProperty("jute.maxbuffer", String.valueOf(8192 * 1024));
	}

	private static void logAfterRunning(Environment env) {
		String appName = StringUtils.trimToEmpty(env.getProperty(SpringProperties.SPRING_APPLICATION_NAME));
		String profile = String.join(Strings.COMMA, SpringProfileUtils.getActiveProfiles(env));
		log.info("\n----------------------------------------------------------\n"
				+ "Application '{}' is running!\n"
				+ "Profile(s):\t{}\n"
				+ getInboundInfo(env)
				+ getOutboundInfo(env)
 				+ "----------------------------------------------------------",
			appName,
			profile);
	}

	private String getInboundInfo(Environment env) {
		ExtensionLoader<EnvironmentInboundParser> extensionLoader = ExtensionLoader.getExtensionLoader(EnvironmentInboundParser.class);
		Set<String> extensions = extensionLoader.getSupportedExtensions();
		StringBuilder logStr = new StringBuilder();
		for (String extension : extensions) {
			String info = extensionLoader.getExtension(extension).toString(env);
			if (StringUtils.isBlank(info)) {
				continue;
			}
			logStr.append(info).append("\n");
		}
		return logStr.toString();
	}

	private String getOutboundInfo(Environment env) {
		ExtensionLoader<EnvironmentOutboundParser> extensionLoader = ExtensionLoader.getExtensionLoader(EnvironmentOutboundParser.class);
		Set<String> extensions = extensionLoader.getSupportedExtensions();
		StringBuilder logStr = new StringBuilder();
		for (String extension : extensions) {
			String info = extensionLoader.getExtension(extension).toString(env);
			if (StringUtils.isBlank(info)) {
				continue;
			}
			logStr.append(info).append("\n");
		}
		return logStr.toString();
	}
}
