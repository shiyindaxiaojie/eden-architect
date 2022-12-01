package org.ylzl.eden.common.cache.hotkey;

import org.ylzl.eden.extension.SPI;

import java.io.Serializable;
import java.util.function.Function;

/**
 * 热key探测接口
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@SPI
public interface HotKey extends Serializable {

	<K> boolean isHotKey(K key, Function<K, Object> builder);
}
