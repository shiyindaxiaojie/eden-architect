package org.ylzl.eden.distributed.lock.spring.boot.autoconfigure.factory;

import lombok.Getter;

/**
 * 短信注册类型
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Getter
public enum DistributedLockBeanType {

	REDISSON(DistributedLockBeanType.REDISSON_DISTRIBUTED_LOCK),
	CURATOR(DistributedLockBeanType.CURATOR_DISTRIBUTED_LOCK),
	JEDIS(DistributedLockBeanType.JEDIS_DISTRIBUTED_LOCK),
	ZOOKEEPER(DistributedLockBeanType.ZOOKEEPER_DISTRIBUTED_LOCK);

	public static final String REDISSON_DISTRIBUTED_LOCK = "redissonDistributedLock";

	public static final String CURATOR_DISTRIBUTED_LOCK = "curatorDistributedLock";

	public static final String JEDIS_DISTRIBUTED_LOCK = "jedisDistributedLock";

	public static final String ZOOKEEPER_DISTRIBUTED_LOCK = "zookeeperDistributedLock";

	private final String lockName;

	DistributedLockBeanType(String lockName) {
		this.lockName = lockName;
	}

	public static DistributedLockBeanType parse(String type) {
		for (DistributedLockBeanType distributedLockBeanType : DistributedLockBeanType.values()) {
			if (distributedLockBeanType.name().equalsIgnoreCase(type)) {
				return distributedLockBeanType;
			}
		}
		return null;
	}
}
