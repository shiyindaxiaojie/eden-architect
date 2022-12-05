package org.ylzl.eden.distributed.uid.spring.boot.support;

import lombok.Getter;

/**
 * 分布式唯一ID注册类型
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Getter
public enum DistributedUIDBeanNames {

	LEAF(DistributedUIDBeanNames.LEAF_DISTRIBUTED_UID),
	SNOWFLAKE(DistributedUIDBeanNames.SNOWFLAKE_DISTRIBUTED_UID),
	TINY_ID(DistributedUIDBeanNames.TINY_ID_DISTRIBUTED_UID),
	UID_GENERATOR(DistributedUIDBeanNames.UID_GENERATOR_DISTRIBUTED_UID);

	public static final String LEAF_DISTRIBUTED_UID = "leafDistributedUID";

	public static final String SNOWFLAKE_DISTRIBUTED_UID = "snowflakeDistributedUID";

	public static final String TINY_ID_DISTRIBUTED_UID = "tinyIdDistributedUID";

	public static final String UID_GENERATOR_DISTRIBUTED_UID = "uidGeneratorDistributedUID";

	private final String beanName;

	DistributedUIDBeanNames(String beanName) {
		this.beanName = beanName;
	}

	public static DistributedUIDBeanNames parse(String type) {
		for (DistributedUIDBeanNames distributedUIDBeanNames : DistributedUIDBeanNames.values()) {
			if (distributedUIDBeanNames.name().equalsIgnoreCase(type)) {
				return distributedUIDBeanNames;
			}
		}
		return null;
	}
}
