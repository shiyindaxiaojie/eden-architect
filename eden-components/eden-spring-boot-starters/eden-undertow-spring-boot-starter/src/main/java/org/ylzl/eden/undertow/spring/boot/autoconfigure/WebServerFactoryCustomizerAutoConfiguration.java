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

package org.ylzl.eden.undertow.spring.boot.autoconfigure;

import io.undertow.server.DefaultByteBufferPool;
import io.undertow.websockets.jsr.WebSocketDeploymentInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Configuration;

/**
 * WebServerFactoryCustomizer 自动配置
 *
 * <br /> Fixed: WARNING Log `Buffer pool was not set on WebSocketDeploymentInfo`
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 * @link https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#web.servlet.embedded-container.customizing.programmatic
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
public class WebServerFactoryCustomizerAutoConfiguration implements WebServerFactoryCustomizer<UndertowServletWebServerFactory> {

	public static final String IO_UNDERTOW_WEBSOCKETS_JSR_WEB_SOCKET_DEPLOYMENT_INFO = "io.undertow.websockets.jsr.WebSocketDeploymentInfo";

	/**
	 * Customize the specified {@link WebServerFactory}.
	 *
	 * @param factory the web server factory to customize
	 */
	@Override
	public void customize(UndertowServletWebServerFactory factory) {
		factory.addDeploymentInfoCustomizers(deploymentInfo -> {
			WebSocketDeploymentInfo webSocketDeploymentInfo = new WebSocketDeploymentInfo();
			webSocketDeploymentInfo.setBuffers(new DefaultByteBufferPool(false, 1024));
			deploymentInfo.addServletContextAttribute(IO_UNDERTOW_WEBSOCKETS_JSR_WEB_SOCKET_DEPLOYMENT_INFO, webSocketDeploymentInfo);
		});
	}
}
