package org.ylzl.eden.spring.boot.undertow.autoconfigure;

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
