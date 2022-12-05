package org.ylzl.eden.distributed.uid.spring.boot.env;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.ylzl.eden.distributed.uid.spring.boot.support.DistributedUIDBeanNames;

/**
 * 分布式唯一ID配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Setter
@Getter
@ConfigurationProperties(prefix = DistributedUIDProperties.PREFIX)
public class DistributedUIDProperties {

	public static final String PREFIX = "distributed-uid";

	private DistributedUIDBeanNames primary;

	private Boolean enabled;

	private final Leaf leaf = new Leaf();

	private final Snowflake snowflake = new Snowflake();

	private final TinyId tinyId = new TinyId();

	private final UidGenerator uidGenerator = new UidGenerator();

	@Setter
	@Getter
	public static class Leaf {

		public static final String PREFIX = DistributedUIDProperties.PREFIX + ".leaf";

		private Boolean enabled;
	}

	@Setter
	@Getter
	public static class Snowflake {

		public static final String PREFIX = DistributedUIDProperties.PREFIX + ".snowflake";

		private Boolean enabled;
	}

	@Setter
	@Getter
	public static class TinyId {

		public static final String PREFIX = DistributedUIDProperties.PREFIX + ".tiny-id";

		private Boolean enabled;
	}

	@Setter
	@Getter
	public static class UidGenerator {

		public static final String PREFIX = DistributedUIDProperties.PREFIX + ".uid-generator";

		private Boolean enabled;
	}
}
