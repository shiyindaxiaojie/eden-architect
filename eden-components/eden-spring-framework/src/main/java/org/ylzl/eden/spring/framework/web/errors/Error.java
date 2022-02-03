package org.ylzl.eden.spring.framework.web.errors;

/**
 * 错误信息接口
 *
 * @author gyl
 * @since 2.4.x
 */
public interface Error {

	String getErrCode();

	String getErrMessage();
}
