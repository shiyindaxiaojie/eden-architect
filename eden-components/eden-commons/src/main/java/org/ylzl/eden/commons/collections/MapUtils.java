package org.ylzl.eden.commons.collections;

import lombok.experimental.UtilityClass;

import java.util.Map;

/**
 * Map 工具集
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@UtilityClass
public class MapUtils {

	public static boolean isEmpty(final Map<?,?> map) {
		return org.apache.commons.collections4.MapUtils.isEmpty(map);
	}

	public static boolean isNotEmpty(final Map<?,?> map) {
		return org.apache.commons.collections4.MapUtils.isNotEmpty(map);
	}
}
