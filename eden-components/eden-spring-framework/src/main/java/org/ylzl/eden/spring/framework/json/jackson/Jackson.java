package org.ylzl.eden.spring.framework.json.jackson;

import org.ylzl.eden.commons.json.JacksonUtils;
import org.ylzl.eden.spring.framework.json.JSON;

/**
 * Jackson
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class Jackson implements JSON {

	/**
	 * 转换为 JSON
	 *
	 * @param object 对象实例
	 * @return JSON格式化内容
	 */
	@Override
	public <T> String toJSONString(T object) {
		return JacksonUtils.toJSONString(object);
	}
}
