package org.ylzl.eden.spring.integration.cat.extension;

import com.dianping.cat.Cat;

import java.util.HashMap;
import java.util.Map;

/**
 * CAT 上下文扩展
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class CatContext implements Cat.Context {

	private final Map<String, String> properties = new HashMap<>(16);

	@Override
	public void addProperty(String key, String value) {
		properties.put(key, value);
	}

	@Override
	public String getProperty(String key) {
		return properties.get(key);
	}
}
