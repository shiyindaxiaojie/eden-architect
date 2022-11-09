package org.ylzl.eden.idempotent.strategy;

import java.util.concurrent.TimeUnit;

/**
 * 基于过期策略管理幂等请求
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public interface ExpiredIdempotentStrategy {


	/**
	 * 检查
	 *
	 * @param key
	 * @param value
	 * @param ttl
	 * @param timeUnit
	 * @return
	 */
	boolean check(String key, Object value, long ttl, TimeUnit timeUnit);
}
