package org.ylzl.eden.distributed.lock.spring.boot.env;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.ylzl.eden.distributed.lock.spring.boot.support.DistributedLockBeanNames;

/**
 * 分布式锁配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Setter
@Getter
@ConfigurationProperties(prefix = DistributedLockProperties.PREFIX)
public class DistributedLockProperties {

	public static final String PREFIX = "distributed-lock";

	private Boolean enabled;

	private DistributedLockBeanNames primary;

	private final Redisson redisson = new Redisson();

	private final Jedis jedis = new Jedis();

	private final Curator curator = new Curator();

	private final ZooKeeper zooKeeper = new ZooKeeper();

	@Setter
	@Getter
	public static class Redisson {

		public static final String PREFIX = DistributedLockProperties.PREFIX + ".redisson";

		private Boolean enabled;
	}

	@Setter
	@Getter
	public static class Jedis {

		public static final String PREFIX = DistributedLockProperties.PREFIX + ".jedis";

		private Boolean enabled;
	}

	@Setter
	@Getter
	public static class Curator {

		public static final String PREFIX = DistributedLockProperties.PREFIX + ".curator";

		private Boolean enabled;
	}

	@Setter
	@Getter
	public static class ZooKeeper {

		public static final String PREFIX = DistributedLockProperties.PREFIX + ".zookeeper";

		private Boolean enabled;
	}
}
