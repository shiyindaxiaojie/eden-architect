package org.ylzl.eden.extension;

/**
 * TODO
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class ExtensionLoaderTest {

	public static void main(String[] args) {
		Demo demo = ExtensionLoader.getExtensionLoader(Demo.class).getExtension("demo1");
		demo.echo();

		Demo defaultDemo = ExtensionLoader.getExtensionLoader(Demo.class).getDefaultExtension();
		defaultDemo.echo();
	}
}
