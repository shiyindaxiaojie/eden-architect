package org.ylzl.eden.distributed.lock.spring.boot.support;

import lombok.Getter;

/**
 * 短信注册Bean
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Getter
public enum DistributedLockBeanNames {

	REDISSON(DistributedLockBeanNames.REDISSON_DISTRIBUTED_LOCK),
	CURATOR(DistributedLockBeanNames.CURATOR_DISTRIBUTED_LOCK),
	JEDIS(DistributedLockBeanNames.JEDIS_DISTRIBUTED_LOCK),
	ZOOKEEPER(DistributedLockBeanNames.ZOOKEEPER_DISTRIBUTED_LOCK);

	public static final String REDISSON_DISTRIBUTED_LOCK = "redissonDistributedLock";

	public static final String CURATOR_DISTRIBUTED_LOCK = "curatorDistributedLock";

	public static final String JEDIS_DISTRIBUTED_LOCK = "jedisDistributedLock";

	public static final String ZOOKEEPER_DISTRIBUTED_LOCK = "zookeeperDistributedLock";

	private final String beanName;

	DistributedLockBeanNames(String beanName) {
		this.beanName = beanName;
	}

	public static DistributedLockBeanNames parse(String type) {
		for (DistributedLockBeanNames distributedLockBeanNames : DistributedLockBeanNames.values()) {
			if (distributedLockBeanNames.name().equalsIgnoreCase(type)) {
				return distributedLockBeanNames;
			}
		}
		return null;
	}
}
