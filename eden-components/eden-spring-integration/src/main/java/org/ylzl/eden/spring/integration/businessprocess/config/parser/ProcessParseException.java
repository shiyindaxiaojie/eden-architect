package org.ylzl.eden.spring.integration.businessprocess.config.parser;

import org.ylzl.eden.spring.framework.error.BaseException;

/**
 * 流程解析异常
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class ProcessParseException extends BaseException {

	public ProcessParseException(String errMessage) {
		super("B0001", errMessage);
	}
}
