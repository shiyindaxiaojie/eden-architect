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

package org.ylzl.eden.spring.framework.logging.access.util;

import lombok.experimental.UtilityClass;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.ylzl.eden.commons.lang.ObjectUtils;
import org.ylzl.eden.commons.lang.StringUtils;
import org.ylzl.eden.commons.lang.Strings;
import org.ylzl.eden.commons.net.IpConfigUtils;
import org.ylzl.eden.spring.framework.json.support.JSONHelper;
import org.ylzl.eden.spring.framework.logging.MdcConstants;
import org.ylzl.eden.spring.framework.logging.access.model.AccessLog;
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
@UtilityClass
public class AccessLogHelper {

	public static final String ACCESS_LOG = "AccessLog";

	public static final Logger log = LoggerFactory.getLogger(ACCESS_LOG);

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
	 * @param invocation    方法拦截
	 * @param result        返回值
	 * @param throwable     异常
	 * @param duration      耗时
	 * @param enabledMdc    是否保存到 MDC
	 * @param maxLength     最大长度
	 * @param slowThreshold 慢访问阈值
	 */
	public static void log(MethodInvocation invocation, Object result, Throwable throwable,
						   long duration, boolean enabledMdc, int maxLength, long slowThreshold) {
		AccessLog accessLog = new AccessLog();
		accessLog.setThrowable(throwable);
		accessLog.setDuration(duration);

		String className = Objects.requireNonNull(invocation.getThis()).getClass().getName();
		String methodName = invocation.getMethod().getName();
		String location = className + Strings.DOT + methodName;
		accessLog.setLocation(location);

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
		accessLog.setArguments(arguments);

		String returnValue = ObjectUtils.isEmpty(result) ? Strings.EMPTY : JSONHelper.json().toJSONString(result);
		if (returnValue.length() > maxLength) {
			returnValue = returnValue.substring(0, maxLength);
		}
		accessLog.setReturnValue(returnValue);

		if (enabledMdc) {
			MDC.put(MdcConstants.CLASS_NAME, className);
			MDC.put(MdcConstants.METHOD_NAME, methodName);
			MDC.put(MdcConstants.ARGUMENTS, StringUtils.trimToEmpty(arguments));
			MDC.put(MdcConstants.RETURN_VALUE, StringUtils.trimToEmpty(returnValue));
			MDC.put(MdcConstants.DURATION, String.valueOf(duration));
		}

		log(accessLog, slowThreshold);
	}

	/**
	 * 输出访问日志
	 *
	 * @param req           请求
	 * @param res           响应
	 * @param throwable     异常
	 * @param duration      耗时
	 * @param enabledMdc    是否保存到 MDC
	 * @param maxLength     最大长度
	 * @param slowThreshold 慢访问阈值
	 */
	public static void log(HttpServletRequest req, HttpServletResponse res, Throwable throwable,
						   long duration, boolean enabledMdc, int maxLength, long slowThreshold) {
		AccessLog accessLog = new AccessLog();
		accessLog.setThrowable(throwable);
		accessLog.setDuration(duration);

		String remoteUser = ServletUtils.getRemoteUser();
		String remoteAddr = IpConfigUtils.parseIpAddress(req);
		String location = req.getRequestURI();
		accessLog.setLocation(location);

		String arguments = ServletUtils.getRequestBody(req);
		if (arguments != null && arguments.length() > maxLength) {
			arguments = arguments.substring(0, maxLength);
		}
		accessLog.setArguments(arguments);

		String returnValue = ServletUtils.getResponseBody(res);
		if (returnValue.length() > maxLength) {
			returnValue = returnValue.substring(0, maxLength);
		}
		accessLog.setReturnValue(returnValue);

		if (enabledMdc) {
			MDC.put(MdcConstants.REMOTE_USER, remoteUser);
			MDC.put(MdcConstants.REMOTE_ADDR, remoteAddr);
			MDC.put(MdcConstants.ARGUMENTS, arguments);
			MDC.put(MdcConstants.RETURN_VALUE, returnValue);
			MDC.put(MdcConstants.DURATION, String.valueOf(duration));
		}

		log(accessLog, slowThreshold);
	}

	/**
	 * 输出访问日志
	 *
	 * @param accessLog     访问日志模型
	 * @param slowThreshold 慢访问阈值
	 */
	public static void log(AccessLog accessLog, long slowThreshold) {
		StringBuilder sb = new StringBuilder();
		sb.append(accessLog.getLocation()).append("(").append(accessLog.getArguments()).append(")");

		if (accessLog.getThrowable() != null) {
			sb.append(" threw exception: ").append(accessLog.getThrowable())
				.append(" (").append(accessLog.getDuration()).append("ms)");
			log.error(sb.toString());
		} else {
			sb.append(" returned: ").append(accessLog.getReturnValue())
				.append(" (").append(accessLog.getDuration()).append("ms)");
			if (accessLog.getDuration() >= slowThreshold) {
				log.warn(sb.toString());
			} else {
				log.info(sb.toString());
			}
		}
	}
}
