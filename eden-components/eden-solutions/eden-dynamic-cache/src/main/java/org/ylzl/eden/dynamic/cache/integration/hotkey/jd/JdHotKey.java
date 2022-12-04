package org.ylzl.eden.dynamic.cache.integration.hotkey.jd;

import com.jd.platform.hotkey.client.callback.JdHotKeyStore;
import org.jetbrains.annotations.NotNull;
import org.ylzl.eden.dynamic.cache.hotkey.HotKey;

import java.util.function.Function;

/**
 * 京东热key探测
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class JdHotKey implements HotKey {

	@Override
	public <K> boolean isHotKey(@NotNull K key, Function<K, Object> function) {
		Object apply = function.apply(key);
		return JdHotKeyStore.isHotKey(apply.toString());
	}
}
