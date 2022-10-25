package org.ylzl.eden.spring.framework.extension;

/**
 * 扩展点工厂接口
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@SPI
public interface ExtensionFactory {

	/**
	 * 获取扩展点
	 *
	 * @param type
	 * @param name
	 * @return
	 * @param <T>
	 */
	<T> T getExtension(Class<T> type, String name);
}
