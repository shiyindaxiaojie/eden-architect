package org.ylzl.eden.spring.framework.json.gson;

import com.google.gson.reflect.TypeToken;
import org.ylzl.eden.spring.framework.json.JSON;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Gson
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class Gson implements JSON {

	private final com.google.gson.Gson gson = new com.google.gson.Gson();

	/**
	 * 转换为 JSON
	 *
	 * @param object 对象实例
	 * @return JSON格式化内容
	 */
	@Override
	public <T> String toJSONString(T object) {
		return gson.toJson(object);
	}

	/**
	 * 解析为对象
	 *
	 * @param text 文本内容
	 * @param cls  目标类型
	 * @return 对象实例
	 */
	@Override
	public <T> T parseObject(String text, Class<T> cls) {
		return gson.fromJson(text, cls);
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
		Type listType = new TypeToken<List<T>>() {}.getType();
		return gson.fromJson(text, listType);
	}
}
