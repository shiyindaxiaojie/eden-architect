package org.ylzl.eden.spring.framework.json.fastjson;

import com.alibaba.fastjson.serializer.SerializeFilter;
import com.google.common.collect.Lists;
import org.ylzl.eden.commons.collections.CollectionUtils;
import org.ylzl.eden.commons.lang.ArrayUtils;
import org.ylzl.eden.extension.ExtensionLoader;
import org.ylzl.eden.spring.framework.json.JSON;

import java.util.List;
import java.util.Set;

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
		SerializeFilter[] filters = loadFilters();
		return ArrayUtils.isNotEmpty(filters)?
			com.alibaba.fastjson.JSON.toJSONString(object, filters):
			com.alibaba.fastjson.JSON.toJSONString(object);
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

	/**
	 * 加载扩展的过滤器
	 *
	 * @return 过滤器数组
	 */
	private SerializeFilter[] loadFilters() {
		ExtensionLoader<FastjsonFilter> extensionLoader = ExtensionLoader.getExtensionLoader(FastjsonFilter.class);
		Set<String> extensions = extensionLoader.getSupportedExtensions();
		if (CollectionUtils.isEmpty(extensions)) {
			return null;
		}
		List<SerializeFilter> filters = Lists.newArrayList();
		for (String extension : extensions) {
			FastjsonFilter filter = extensionLoader.getExtension(extension);
			filters.add(filter);
		}
		return filters.toArray(new SerializeFilter[0]);
	}
}
