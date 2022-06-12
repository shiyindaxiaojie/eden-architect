package org.ylzl.eden.spring.integration.businessprocess.process;

import org.ylzl.eden.spring.framework.error.BaseException;

/**
 * 流程定义无效异常
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class ProcessDefinitionException extends BaseException {

	public ProcessDefinitionException(String errMessage) {
		super("B0001", errMessage);
	}
}
