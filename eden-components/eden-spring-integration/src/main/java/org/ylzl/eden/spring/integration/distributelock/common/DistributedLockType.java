package org.ylzl.eden.spring.integration.distributelock.common;

import lombok.experimental.UtilityClass;

/**
 * 分布式锁类型（内部）
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@UtilityClass
public class DistributedLockType {

	public static final String REDISSON = "Redisson";

	public static final String CURATOR = "Curator";

	public static final String JEDIS = "Jedis";

	public static final String ZOOKEEPER = "Zookeeper";
}
