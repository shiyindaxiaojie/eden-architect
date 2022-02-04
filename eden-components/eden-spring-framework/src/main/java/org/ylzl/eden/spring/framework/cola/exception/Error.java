package org.ylzl.eden.spring.framework.cola.exception;

/**
 * 错误信息接口
 *
 * @author gyl
 * @since 2.4.x
 */
public interface Error {

	String getErrCode();

	String getErrMessage();

	int getHttpStatusCode();
}
