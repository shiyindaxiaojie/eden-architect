package org.ylzl.eden.distributed.lock.exception;

import org.ylzl.eden.spring.framework.error.BaseException;

/**
 * 分布式锁异常
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class DistributedLockException extends BaseException {

	public DistributedLockException(String errMessage) {
		super("SYS-ERROR-500", errMessage);
	}
}
