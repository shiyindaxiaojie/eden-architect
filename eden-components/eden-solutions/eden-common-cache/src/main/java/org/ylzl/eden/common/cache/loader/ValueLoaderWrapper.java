package org.ylzl.eden.common.cache.loader;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ValueLoader 包装器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
@Slf4j
public class ValueLoaderWrapper implements Callable<Object> {

	/** 等待刷新的计数器，大于0表示有等待执行或正在执行的刷新操作，等于0表示可以执行刷新操作 */
	private final AtomicInteger waitRefreshCount = new AtomicInteger();

	private final String cacheName;

	private final Object key;

	private final Callable<?> valueLoader;

	@Override
	public Object call() throws Exception {
		try {
			if (valueLoader == null) {
				log.warn("ValueLoader is null, return null, cacheName={}, key={}", cacheName, key);
				return null;
			}
			return valueLoader.call();
		} finally {
			if (this.waitRefreshCount.get() > 0) {
				this.waitRefreshCount.getAndSet(0);
				log.debug("Clear waitRefreshCount, cacheName={}, key={}", cacheName, key);
			}
		}
	}
}
