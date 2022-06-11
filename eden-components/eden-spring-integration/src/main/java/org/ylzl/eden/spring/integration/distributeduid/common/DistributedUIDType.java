package org.ylzl.eden.spring.integration.distributeduid.common;

import lombok.experimental.UtilityClass;

/**
 * 分布式锁类型（内部）
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@UtilityClass
public class DistributedUIDType {

	public static final String LEAF = "Leaf";

	public static final String SNOWFLAKE = "Snowflake";

	public static final String TINY_ID = "Tinyid";

	public static final String UID_GENERATOR = "UidGenerator";
}
