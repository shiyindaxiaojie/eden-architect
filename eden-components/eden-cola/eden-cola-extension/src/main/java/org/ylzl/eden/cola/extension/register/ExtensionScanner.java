package org.ylzl.eden.cola.extension.register;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.ylzl.eden.cola.extension.core.Extension;
import org.ylzl.eden.cola.extension.core.Extensions;

import java.util.Map;

/**
 * 扩展点扫描
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@RequiredArgsConstructor
@Slf4j
public class ExtensionScanner implements ApplicationContextAware, InitializingBean {

	private ApplicationContext applicationContext;

	private final ExtensionRegister extensionRegister;

	@Override
	public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Map<String, Object> extensionBeans = applicationContext.getBeansWithAnnotation(Extension.class);
		extensionBeans.values().forEach(extensionRegister::registerExtension);

		Map<String, Object> extensionsBeans = applicationContext.getBeansWithAnnotation(Extensions.class);
		extensionsBeans.values().forEach(extensionRegister::registerExtension);
	}
}
