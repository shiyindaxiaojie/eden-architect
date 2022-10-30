package org.ylzl.eden.common.cache.core.loader;

import org.ylzl.eden.spring.framework.extension.SPI;

import java.io.Serializable;

/**
 * 缓存加载器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@SPI
public interface CacheLoader<K, V> extends Serializable {

}
