package org.ylzl.eden.spring.integration.distributeduid.core;

import java.util.List;

/**
 * 分布式唯一ID
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public interface DistributedUID {

	long generateUID();

	List<Long> generateUIDs();
}
