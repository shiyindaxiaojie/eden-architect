package org.ylzl.eden.spring.integration.distributelock.curator;

import lombok.RequiredArgsConstructor;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.ylzl.eden.commons.lang.StringConstants;
import org.ylzl.eden.spring.integration.distributelock.core.DistributeLock;
import org.ylzl.eden.spring.integration.distributelock.core.DistributedLockManager;

/**
 * Curator 分布式锁管理器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@RequiredArgsConstructor
public class CuratorDistributedLockManager implements DistributedLockManager {

	private final CuratorFramework curatorFramework;

	/**
	 * 获取分布式锁
	 *
	 * @param name 标识名称
	 * @return 分布式锁
	 */
	@Override
	public DistributeLock getLock(String name) {
		if (!name.startsWith(StringConstants.SLASH)) {
			throw new IllegalArgumentException("Invalid curator distribute lock: " + name);
		}

		InterProcessMutex interProcessMutex = new InterProcessMutex(curatorFramework, name);
		return new CuratorDistributeLock(interProcessMutex);
	}
}
