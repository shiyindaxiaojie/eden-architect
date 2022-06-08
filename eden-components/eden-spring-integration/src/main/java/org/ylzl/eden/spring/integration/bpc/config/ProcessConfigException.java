package org.ylzl.eden.spring.integration.bpc.config;

import org.ylzl.eden.spring.framework.error.BaseException;

/**
 * 流程配置无效异常
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class ProcessConfigException extends BaseException {

	public ProcessConfigException(String errMessage) {
		super("B0001", errMessage);
	}
}
