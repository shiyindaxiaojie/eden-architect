package org.ylzl.eden.common.cache.core.hotkey;

import java.io.Serializable;
import java.util.function.Function;

/**
 * 热key探测接口
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public interface HotKey extends Serializable {

	<K> boolean isHotKey(K key, Function<K, Object> builder);
}
