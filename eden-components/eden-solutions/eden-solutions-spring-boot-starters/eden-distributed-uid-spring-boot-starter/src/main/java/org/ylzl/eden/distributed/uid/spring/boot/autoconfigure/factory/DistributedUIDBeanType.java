package org.ylzl.eden.distributed.uid.spring.boot.autoconfigure.factory;

import lombok.Getter;

/**
 * 分布式唯一ID注册类型
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Getter
public enum DistributedUIDBeanType {

	LEAF(DistributedUIDBeanType.LEAF_DISTRIBUTED_UID),
	SNOWFLAKE(DistributedUIDBeanType.SNOWFLAKE_DISTRIBUTED_UID),
	TINY_ID(DistributedUIDBeanType.TINY_ID_DISTRIBUTED_UID),
	UID_GENERATOR(DistributedUIDBeanType.UID_GENERATOR_DISTRIBUTED_UID);

	public static final String LEAF_DISTRIBUTED_UID = "leafDistributedUID";

	public static final String SNOWFLAKE_DISTRIBUTED_UID = "snowflakeDistributedUID";

	public static final String TINY_ID_DISTRIBUTED_UID = "tinyIdDistributedUID";

	public static final String UID_GENERATOR_DISTRIBUTED_UID = "uidGeneratorDistributedUID";

	private final String uidName;

	DistributedUIDBeanType(String uidName) {
		this.uidName = uidName;
	}

	public static DistributedUIDBeanType parse(String type) {
		for (DistributedUIDBeanType distributedUIDBeanType : DistributedUIDBeanType.values()) {
			if (distributedUIDBeanType.name().equalsIgnoreCase(type)) {
				return distributedUIDBeanType;
			}
		}
		return null;
	}
}
