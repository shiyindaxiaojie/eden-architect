package org.ylzl.eden.cache.adapter.hotkey;

/**
 * 热key 类型
 *
 * @author <a href="mailto:guoyuanlu@puyiwm.com">gyl</a>
 * @since 1.0.0
 */
public enum HotkeyType {

	NONE,
	JD;

	public static HotkeyType toHotkeyType(String type) {
		for (HotkeyType cacheType : HotkeyType.values()) {
			if (cacheType.name().equalsIgnoreCase(type)) {
				return cacheType;
			}
		}
		return null;
	}
}
