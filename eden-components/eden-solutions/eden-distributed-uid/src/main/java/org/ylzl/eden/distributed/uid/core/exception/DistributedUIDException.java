package org.ylzl.eden.distributed.uid.core.exception;

import org.ylzl.eden.spring.framework.error.BaseException;

/**
 * 分布式ID异常
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class DistributedUIDException extends BaseException {

	public DistributedUIDException(String errMessage) {
		super("SYS-ERROR-500", errMessage);
	}
}
