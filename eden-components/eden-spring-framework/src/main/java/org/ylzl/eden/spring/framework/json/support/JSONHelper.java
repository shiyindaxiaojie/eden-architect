package org.ylzl.eden.spring.framework.json.support;

import org.ylzl.eden.extension.ExtensionLoader;
import org.ylzl.eden.spring.framework.json.JSON;

/**
 * JSON 帮助支持
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class JSONHelper {

	/**
	 * 获取 JSON 默认实现
	 *
	 * @return JSON 实例
	 */
	public static JSON json() {
		return ExtensionLoader.getExtensionLoader(JSON.class).getDefaultExtension();
	}

	/**
	 * 获取 JSON 实现
	 *
	 * @param spi 扩展点类型
	 * @return JSON 实例
	 */
	public static JSON json(String spi) {
		return ExtensionLoader.getExtensionLoader(JSON.class).getExtension(spi);
	}
}
