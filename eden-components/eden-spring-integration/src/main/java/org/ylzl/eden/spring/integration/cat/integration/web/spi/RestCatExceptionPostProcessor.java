package org.ylzl.eden.spring.integration.cat.integration.web.spi;

import org.ylzl.eden.spring.framework.web.rest.handler.RestExceptionPostProcessor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Rest 异常后置处理
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class RestCatExceptionPostProcessor implements RestExceptionPostProcessor {

	public static final String SPI = "cat";

	private static final String CAT_ERROR_KEY = "_CAT_ERROR";

	/**
	 * 处理异常
	 *
	 * @param request  当前请求
	 * @param response 响应
	 * @param e        异常
	 */
	@Override
	public void postProcess(HttpServletRequest request, HttpServletResponse response, Throwable e) {
		request.setAttribute(CAT_ERROR_KEY, e);
	}

	/**
	 * 获取当前异常
	 *
	 * @param request  当前请求
	 * @param response 响应
	 * @return 异常
	 */
	@Override
	public Throwable getThrowable(HttpServletRequest request, HttpServletResponse response) {
		Throwable throwable = (Throwable) request.getAttribute(CAT_ERROR_KEY);
		request.removeAttribute(CAT_ERROR_KEY);
		return throwable;
	}
}
