package org.ylzl.eden.distributed.lock.exception;

import org.ylzl.eden.spring.framework.error.BaseException;

/**
 * 分布式锁释放异常
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class DistributedLockReleaseException extends BaseException {

	public DistributedLockReleaseException(String errMessage) {
		super("LOCK-RELEASE-500", errMessage);
	}

	public DistributedLockReleaseException(Throwable ex) {
		super("LOCK-RELEASE-500", ex);
	}
}
