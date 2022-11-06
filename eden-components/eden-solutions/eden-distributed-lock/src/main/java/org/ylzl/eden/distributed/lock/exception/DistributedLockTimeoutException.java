package org.ylzl.eden.distributed.lock.exception;

import org.ylzl.eden.spring.framework.error.BaseException;

/**
 * 分布式锁超时异常
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class DistributedLockTimeoutException extends BaseException {

	public DistributedLockTimeoutException(String errMessage) {
		super("LOCK-TIMEOUT-500", errMessage);
	}

	public DistributedLockTimeoutException(Throwable ex) {
		super("LOCK-TIMEOUT-500", ex);
	}
}
