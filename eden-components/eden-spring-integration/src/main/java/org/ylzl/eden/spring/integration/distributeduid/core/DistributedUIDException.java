package org.ylzl.eden.spring.integration.distributeduid.core;

import org.ylzl.eden.spring.framework.error.BaseException;

/**
 * 分布式ID异常
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class DistributedUIDException extends BaseException {

	public DistributedUIDException(String errMessage) {
		super("B0001", errMessage);
	}
}
