package org.ylzl.eden.extension.factory;

import org.ylzl.eden.extension.ExtensionFactory;
import org.ylzl.eden.extension.ExtensionLoader;
import org.ylzl.eden.extension.SPI;

/**
 * SPI 扩展点实例工厂
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class SpiExtensionFactory implements ExtensionFactory {

	@Override
	public <T> T getExtension(Class<T> type, String name) {
		if (type.isInterface() && type.isAnnotationPresent(SPI.class)) {
			ExtensionLoader<T> loader = ExtensionLoader.getExtensionLoader(type);
			if (!loader.getSupportedExtensions().isEmpty()) {
				return loader.getAdaptiveExtension();
			}
		}
		return null;
	}
}
