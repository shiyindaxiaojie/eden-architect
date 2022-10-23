package org.ylzl.eden.cache.adapter.core.hotkey;

/**
 * 热key 类型
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 1.0.0
 */
public enum HotkeyType {

	NONE,
	JD,
	SENTINEL;

	public static HotkeyType toHotkeyType(String type) {
		for (HotkeyType cacheType : HotkeyType.values()) {
			if (cacheType.name().equalsIgnoreCase(type)) {
				return cacheType;
			}
		}
		return null;
	}
}
