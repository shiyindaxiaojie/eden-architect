package org.ylzl.eden.spring.test.embedded;

import org.ylzl.eden.extension.ExtensionLoader;

/**
 * TODO
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class EmbeddedServerBuilder {

	public static EmbeddedServer build(String spi, int port) {
		return ExtensionLoader.getExtensionLoader(EmbeddedServer.class).getExtension(spi).port(port);
	}
}
