package org.ylzl.eden.distributed.uid.core;

import java.util.List;

/**
 * 分布式唯一ID
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public interface DistributedUID {

	/**
	 * 生成 UID
	 *
	 * @return
	 */
	long generateUID();

	/**
	 * 批量生成 UID
	 *
	 * @return
	 */
	List<Long> generateUIDs();
}
