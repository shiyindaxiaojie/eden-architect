package org.ylzl.eden.spring.framework.json;

import org.ylzl.eden.extension.SPI;

import java.util.List;

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

	/**
	 * 解析为对象
	 *
	 * @param text 文本内容
	 * @param cls 目标类型
	 * @return 对象实例
	 * @param <T> 泛型
	 */
	<T> T parseObject(String text, Class<T> cls);

	/**
	 * 解析为对象列表
	 *
	 * @param text 文本内容
	 * @param cls 目标类型
	 * @return 对象实例列表
	 * @param <T> 泛型
	 */
	<T> List<T> parseList(String text, Class<T> cls);
}
