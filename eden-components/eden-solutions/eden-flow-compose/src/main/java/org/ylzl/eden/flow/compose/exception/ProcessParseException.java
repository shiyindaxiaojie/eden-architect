package org.ylzl.eden.flow.compose.exception;

import org.ylzl.eden.spring.framework.error.BaseException;

/**
 * 流程解析异常
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class ProcessParseException extends BaseException {

	public ProcessParseException(String errMessage) {
		super("SYS-ERROR-500", errMessage);
	}
}
