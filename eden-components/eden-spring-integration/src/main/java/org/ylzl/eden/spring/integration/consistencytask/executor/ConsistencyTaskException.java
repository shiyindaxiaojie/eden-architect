package org.ylzl.eden.spring.integration.consistencytask.executor;

import org.ylzl.eden.spring.framework.error.BaseException;

/**
 * 一致性任务异常
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class ConsistencyTaskException extends BaseException {

	public ConsistencyTaskException(String errMessage) {
		super("B0001", errMessage);
	}
}
