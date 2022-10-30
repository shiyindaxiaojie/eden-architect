package org.ylzl.eden.common.cache.core.hotkey;

/**
 * 热key类型
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public enum HotkeyType {

	NONE,
	JD,
	SENTINEL;

	public static HotkeyType parse(String type) {
		for (HotkeyType cacheType : HotkeyType.values()) {
			if (cacheType.name().equalsIgnoreCase(type)) {
				return cacheType;
			}
		}
		return null;
	}
}
