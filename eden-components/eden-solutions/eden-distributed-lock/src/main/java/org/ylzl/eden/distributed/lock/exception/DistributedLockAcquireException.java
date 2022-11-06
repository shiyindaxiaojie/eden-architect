package org.ylzl.eden.distributed.lock.exception;

import org.ylzl.eden.spring.framework.error.BaseException;

/**
 * 分布式锁申请异常
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class DistributedLockAcquireException extends BaseException {

	public DistributedLockAcquireException(String errMessage) {
		super("LOCK-ACQUIRE-500", errMessage);
	}

	public DistributedLockAcquireException(Throwable ex) {
		super("LOCK-ACQUIRE-500", ex);
	}
}
