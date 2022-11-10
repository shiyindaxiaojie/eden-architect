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
	 * 检查是否首次请求
	 *
	 * @param key 键
	 * @param value 值
	 * @param ttl 存活时间
	 * @param timeUnit 时间单位
	 * @return 是否通过
	 */
	boolean checkFirstRequest(String key, Object value, long ttl, TimeUnit timeUnit);

	/**
	 * 调用后执行释放
	 *
	 * @param key 键
	 */
	void releaseAfterInvoke(String key);
}
