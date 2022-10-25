package org.ylzl.eden.spring.framework.extension.active;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ylzl.eden.commons.lang.StringUtils;
import org.ylzl.eden.spring.framework.extension.Activate;
import org.ylzl.eden.spring.framework.extension.ExtensionLoader;
import org.ylzl.eden.spring.framework.extension.common.Constants;
import org.ylzl.eden.spring.framework.extension.common.URL;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Arrays.asList;

/**
 * 基于 @Active 的扩展点加载器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
@Slf4j
public class ActiveExtensionLoader<T> {

	public static final String REMOVE_VALUE_PREFIX = "-";

	public static final String DEFAULT_KEY = "default";

	/** 缓存激活的扩展点 */
	private final Map<String, Object> cachedActivates = new ConcurrentHashMap<>();

	private final ExtensionLoader<T> extensionLoader;

	/**
	 * 缓存 @Activate 扩展类
	 *
	 * @param clazz
	 * @param name
	 */
	public void cacheActivateClass(Class<?> clazz, String name) {
		Activate activate = clazz.getAnnotation(Activate.class);
		if (activate != null) {
			cachedActivates.put(name, activate);
		}
	}

	/**
	 * 获取 @Activate 扩展点
	 *
	 * @param url
	 * @param values
	 * @param group
	 * @return
	 */
	public List<T> getActivateExtension(URL url, String[] values, String group) {
		List<T> activateExtensions = new ArrayList<>();
		List<String> names = values == null ? new ArrayList<>(0) : asList(values);
		if (!names.contains(REMOVE_VALUE_PREFIX + DEFAULT_KEY)) {
			extensionLoader.getExtensionClasses();
			for (Map.Entry<String, Object> entry : cachedActivates.entrySet()) {
				String name = entry.getKey();
				Object activate = entry.getValue();
				String[] activateGroup;
				String[] activateValue;
				if (activate instanceof Activate) {
					activateGroup = ((Activate) activate).group();
					activateValue = ((Activate) activate).value();
				} else {
					continue;
				}
				if (isMatchGroup(group, activateGroup)
					&& !names.contains(name)
					&& !names.contains(REMOVE_VALUE_PREFIX + name)
					&& isActive(activateValue, url)) {
					activateExtensions.add(extensionLoader.getExtension(name));
				}
			}
			activateExtensions.sort(ActivateComparator.COMPARATOR);
		}
		List<T> loadedExtensions = new ArrayList<>();
		for (int i = 0; i < names.size(); i++) {
			String name = names.get(i);
			if (!name.startsWith(REMOVE_VALUE_PREFIX)
				&& !names.contains(REMOVE_VALUE_PREFIX + name)) {
				if (DEFAULT_KEY.equals(name)) {
					if (!loadedExtensions.isEmpty()) {
						activateExtensions.addAll(0, loadedExtensions);
						loadedExtensions.clear();
					}
				} else {
					loadedExtensions.add(extensionLoader.getExtension(name));
				}
			}
		}
		if (!loadedExtensions.isEmpty()) {
			activateExtensions.addAll(loadedExtensions);
		}
		return activateExtensions;
	}

	public List<T> getActivateExtension(URL url, String key) {
		return getActivateExtension(url, key, null);
	}

	public List<T> getActivateExtension(URL url, String key, String group) {
		String value = url.getParameter(key);
		return getActivateExtension(url, StringUtils.isEmpty(value) ? null :
				Constants.COMMA_SPLIT_PATTERN.split(value), group);
	}

	public List<T> getActivateExtension(URL url, String[] values) {
		return getActivateExtension(url, values, null);
	}

	private boolean isMatchGroup(String group, String[] groups) {
		if (StringUtils.isEmpty(group)) {
			return true;
		}
		if (groups != null && groups.length > 0) {
			for (String g : groups) {
				if (group.equals(g)) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean isActive(String[] keys, URL url) {
		if (keys.length == 0) {
			return true;
		}
		for (String key : keys) {
			String keyValue = null;
			if (key.contains(":")) {
				String[] arr = key.split(":");
				key = arr[0];
				keyValue = arr[1];
			}

			/*for (Map.Entry<String, String> entry : url.getParameters().entrySet()) {
				String k = entry.getKey();
				String v = entry.getValue();
				if ((k.equals(key) || k.endsWith("." + key))
					&& ((keyValue != null && keyValue.equals(v)) || (keyValue == null && ConfigUtils.isNotEmpty(v)))) {
					return true;
				}
			}*/
		}
		return false;
	}
}
