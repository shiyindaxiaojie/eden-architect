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
import org.ylzl.eden.commons.lang.math.NumberUtils;
import org.ylzl.eden.spring.boot.bootstrap.util.SpringProfileUtils;
import org.ylzl.eden.spring.framework.bootstrap.constant.SpringProperties;

import java.net.InetAddress;

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
			SpringProfileUtils.addDefaultProfile(app);
			app.setBannerMode(Banner.Mode.OFF);

			ConfigurableApplicationContext context = app.run(args);
			Environment env = context.getEnvironment();
			logApplicationServerAfterRunning(env);
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

	private static void logApplicationServerAfterRunning(Environment env) {
		String appName = StringUtils.trimToEmpty(env.getProperty(SpringProperties.SPRING_APPLICATION_NAME));
		String profile = StringUtils.trimToEmpty(env.getProperty(SpringProperties.SPRING_PROFILE_DEFAULT));
		String contextPath = StringUtils.trimToEmpty(env.getProperty("server.servlet.context-path"));
		int serverPort = NumberUtils.toInt(env.getProperty("server.port"));
		String protocol = env.containsProperty("server.ssl.key-store") ? "https" : "http";
		String localhostAddress = "localhost";
		String hostAddress;
		try {
			hostAddress = InetAddress.getLocalHost().getHostAddress();
		} catch (Exception e) {
			hostAddress = localhostAddress;
			log.warn("The host name could not be determined, using ‘localhost‘ as fallback");
		}

		log.info("\n----------------------------------------------------------\n\t"
				+ "Application '{}' is running! \n\t"
				+ "Profile(s): \t{}\n\t"
				+ "Local Access URL: \t{}://{}:{}{}\n\t"
				+ "External Access URL: \t{}://{}:{}{}"
				+ "\n----------------------------------------------------------",
			appName,
			profile,
			protocol, localhostAddress, serverPort, contextPath,
			protocol, hostAddress, serverPort, contextPath);
	}
}
