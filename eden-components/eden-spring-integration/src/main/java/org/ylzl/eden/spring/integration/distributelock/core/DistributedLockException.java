package org.ylzl.eden.spring.integration.distributelock.core;

import org.ylzl.eden.spring.framework.error.BaseException;

/**
 * 分布式锁异常
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class DistributedLockException extends BaseException {

	public DistributedLockException(String errMessage) {
		super("B0320", errMessage);
	}
}
