package org.ylzl.eden.cache.adapter.core.hotkey;

import java.io.Serializable;
import java.util.function.Function;

/**
 * 热key
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public interface HotKey extends Serializable {

	<K> boolean isHotKey(K key, Function<K, Object> builder);
}
