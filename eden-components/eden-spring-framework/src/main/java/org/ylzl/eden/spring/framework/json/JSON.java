package org.ylzl.eden.spring.framework.json;

import org.ylzl.eden.extension.SPI;

/**
 * JSON 格式化
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@SPI("jackson")
public interface JSON {

	/**
	 * 转换为 JSON
	 *
	 * @param object 对象实例
	 * @return JSON格式化内容
	 * @param <T> 泛型
	 */
	<T> String toJSONString(T object);
}
