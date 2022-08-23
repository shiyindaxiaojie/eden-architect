package org.ylzl.eden.bussiness.process.choreography.process;

import org.ylzl.eden.spring.framework.error.BaseException;

/**
 * 流程节点无效异常
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class ProcessNodeException extends BaseException {

	public ProcessNodeException(String errMessage) {
		super("B0001", errMessage);
	}
}
