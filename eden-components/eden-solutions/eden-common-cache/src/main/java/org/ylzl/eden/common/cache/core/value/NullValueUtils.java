package org.ylzl.eden.common.cache.core.value;

import lombok.experimental.UtilityClass;
import org.ylzl.eden.spring.framework.error.util.MessageFormatUtils;

/**
 * NullValue 工具集
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@UtilityClass
public class NullValueUtils {

	/**
	 * 转换为存储值
	 *
	 * @param value
	 * @param allowNullValues
	 * @param cacheName
	 * @return
	 */
	public static Object toStoreValue(Object value, boolean allowNullValues, String cacheName) {
		if (value == null) {
			if (allowNullValues) {
				return NullValue.INSTANCE;
			}
			throw new IllegalArgumentException(MessageFormatUtils.format(
				"Cache `{}` is configured to not allow null values but null was provided", cacheName));
		}
		return value;
	}

	/**
	 * 从存储值解析为具体值
	 */
	public static Object fromStoreValue(Object storeValue, boolean allowNullValues) {
		if (storeValue instanceof NullValue && allowNullValues) {
			return null;
		}
		return storeValue;
	}
}
