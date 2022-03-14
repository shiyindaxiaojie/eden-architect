package org.ylzl.eden.spring.integration.distributelock.redisson;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.ylzl.eden.spring.integration.distributelock.core.DistributeLock;
import org.ylzl.eden.spring.integration.distributelock.core.DistributedLockManager;

/**
 * Redisson 分布式锁管理器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@RequiredArgsConstructor
public class RedissonDistributedLockManager implements DistributedLockManager {

	private final RedissonClient redissonClient;

	/**
	 * 获取分布式锁
	 *
	 * @param name 标识名称
	 * @return 分布式锁
	 */
	@Override
	public DistributeLock getLock(String name) {
		RLock rLock = redissonClient.getFairLock(name);
		return new RedissonDistributeLock(rLock);
	}
}
