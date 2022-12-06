package org.ylzl.eden.cola.extension.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.cola.extension.executor.ExtensionExecutor;
import org.ylzl.eden.cola.extension.register.ExtensionRegister;
import org.ylzl.eden.cola.extension.register.ExtensionScanner;

/**
 * 扩展点配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
public class ExtensionConfiguration {

	@Bean
	public ExtensionRegister extensionRegister() {
		return new ExtensionRegister();
	}

	@Bean
	public ExtensionScanner extensionScanner(ExtensionRegister extensionRegister) {
		return new ExtensionScanner(extensionRegister);
	}

	@Bean
	public ExtensionExecutor extensionExecutor(ExtensionRegister extensionRegister) {
		return new ExtensionExecutor(extensionRegister);
	}
}
