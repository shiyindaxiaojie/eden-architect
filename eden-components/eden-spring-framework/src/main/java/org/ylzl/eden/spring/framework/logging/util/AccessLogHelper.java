/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ylzl.eden.spring.framework.logging.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.MDC;
import org.ylzl.eden.commons.lang.ObjectUtils;
import org.ylzl.eden.commons.lang.Strings;
import org.ylzl.eden.spring.framework.logging.config.AccessLogConstants;
import org.ylzl.eden.spring.framework.web.util.ServletUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * 访问日志支持
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Slf4j
@UtilityClass
public class AccessLogHelper {

	/**
	 * 根据采样率判断是否需要输出日志
	 *
	 * @return 是否输出
	 */
	public static boolean shouldLog(double sampleRate) {
		return sampleRate >= 1.0 || Math.random() < sampleRate;
	}

	/**
	 * 输出访问日志
	 *
	 * @param invocation 方法拦截
	 * @param result 返回值
	 * @param throwable 异常
	 * @param duration 耗时
	 * @param enabledMdc 是否保存到 MDC
	 * @param maxLength 最大长度
	 */
	public static void log(MethodInvocation invocation, Object result, Throwable throwable,
						   long duration, boolean enabledMdc, int maxLength) {
		String className = Objects.requireNonNull(invocation.getThis()).getClass().getName();
		String methodName = invocation.getMethod().getName();
		String location = className + Strings.DOT + methodName;

		Object[] args = invocation.getArguments();
		StringBuilder argsBuilder = new StringBuilder();
		for (int i = 0; i < args.length; i++) {
			if (i > 0) {
				argsBuilder.append(", ");
			}
			argsBuilder.append(args[i] == null ? Strings.NULL : args[i].toString());
		}
		String arguments = argsBuilder.toString();
		if (arguments.length() > maxLength) {
			arguments = arguments.substring(0, maxLength);
		}

		String returnValue = ObjectUtils.trimToString(result);
		if (returnValue.length() > maxLength) {
			returnValue = returnValue.substring(0, maxLength);
		}

		if (enabledMdc) {
			MDC.put(AccessLogConstants.CLASS_NAME, className);
			MDC.put(AccessLogConstants.METHOD_NAME, methodName);
			MDC.put(AccessLogConstants.LOCATION, location);
			MDC.put(AccessLogConstants.ARGUMENTS, arguments);
			MDC.put(AccessLogConstants.RETURN_VALUE, returnValue);
			MDC.put(AccessLogConstants.DURATION, String.valueOf(duration));
		}

		log(location, arguments, returnValue, throwable, duration);
	}

	/**
	 * 输出访问日志
	 *
	 * @param req 请求
	 * @param res 响应
	 * @param throwable 异常
	 * @param duration 耗时
	 * @param enabledMdc 是否保存到 MDC
	 * @param maxLength 最大长度
	 */
	public static void log(HttpServletRequest req, HttpServletResponse res, Throwable throwable,
						   long duration, boolean enabledMdc, int maxLength) {
		String remoteUser = ServletUtils.getRemoteUser();
		String remoteAddr = ServletUtils.getRemoteAddr();
		String location = req.getRequestURI();

		String arguments = ServletUtils.getRequestParameters(req) + Strings.COMMA + ServletUtils.getRequestBody(req);
		if (arguments.length() > maxLength) {
			arguments = arguments.substring(0, maxLength);
		}

		String returnValue = ServletUtils.getResponseBody(res);
		if (returnValue.length() > maxLength) {
			returnValue = returnValue.substring(0, maxLength);
		}

		if (enabledMdc) {
			MDC.put(AccessLogConstants.REMOTE_USER, remoteUser);
			MDC.put(AccessLogConstants.REMOTE_ADDR, remoteAddr);
			MDC.put(AccessLogConstants.LOCATION, location);
			MDC.put(AccessLogConstants.ARGUMENTS, arguments);
			MDC.put(AccessLogConstants.RETURN_VALUE, returnValue);
			MDC.put(AccessLogConstants.DURATION, String.valueOf(duration));
		}

		log(location, arguments, returnValue, throwable, duration);
	}

	/**
	 * 输出访问日志
	 *
	 * @param location 执行位置
	 * @param arguments 参数
	 * @param returnValue 返回值
	 * @param throwable 异常
	 * @param duration 耗时
	 */
	public static void log(String location, String arguments, String returnValue,
						   Throwable throwable, long duration) {
		StringBuilder sb = new StringBuilder();
		sb.append(location).append("(").append(arguments).append(")");

		if (throwable != null) {
			sb.append(" threw exception: ").append(throwable);
		} else {
			sb.append(" returned: ").append(returnValue);
		}

		sb.append(" (").append(duration).append("ms)");

		log.info(sb.toString());
	}
}
