package org.ylzl.eden.flow.compose.core.process.exception;

import org.ylzl.eden.spring.framework.error.BaseException;

/**
 * 流程节点无效异常
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class ProcessNodeException extends BaseException {

	public ProcessNodeException(String errMessage) {
		super("SYS-ERROR-500", errMessage);
	}
}
