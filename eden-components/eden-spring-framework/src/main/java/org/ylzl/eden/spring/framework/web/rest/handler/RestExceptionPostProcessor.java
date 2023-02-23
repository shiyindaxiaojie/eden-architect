package org.ylzl.eden.spring.framework.web.rest.handler;

import org.ylzl.eden.extension.SPI;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Rest 异常后置处理
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@SPI
public interface RestExceptionPostProcessor {

	/**
	 * 处理异常
	 *
	 * @param request 当前请求
	 * @param response 响应
	 * @param e 异常
	 */
	void postProcess(HttpServletRequest request, HttpServletResponse response, Throwable e);

	/**
	 * 获取当前异常
	 *
	 * @param request  当前请求
	 * @param response 响应
	 * @return 异常
	 */
	Throwable getThrowable(HttpServletRequest request, HttpServletResponse response);
}
