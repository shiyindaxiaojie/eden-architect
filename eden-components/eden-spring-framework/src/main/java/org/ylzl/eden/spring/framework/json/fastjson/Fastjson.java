package org.ylzl.eden.spring.framework.json.fastjson;

import org.ylzl.eden.spring.framework.json.JSON;

import java.util.List;

/**
 * Fastjson
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class Fastjson implements JSON {

	/**
	 * 转换为 JSON
	 *
	 * @param object 对象实例
	 * @return JSON格式化内容
	 */
	@Override
	public <T> String toJSONString(T object) {
		return com.alibaba.fastjson.JSON.toJSONString(object);
	}

	/**
	 * 解析为对象
	 *
	 * @param text  文本内容
	 * @param cls 目标类型
	 * @return 对象实例
	 */
	@Override
	public <T> T parseObject(String text, Class<T> cls) {
		return com.alibaba.fastjson.JSON.parseObject(text, cls);
	}

	/**
	 * 解析为对象列表
	 *
	 * @param text 文本内容
	 * @param cls  目标类型
	 * @return 对象实例列表
	 */
	@Override
	public <T> List<T> parseList(String text, Class<T> cls) {
		return com.alibaba.fastjson.JSON.parseArray(text, cls);
	}
}
