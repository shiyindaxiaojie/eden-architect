/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ylzl.eden.cola.catchlog.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.ylzl.eden.cola.catchlog.handler.CatchLogHandler;
import org.ylzl.eden.spring.framework.error.BaseException;
import org.ylzl.eden.spring.framework.error.ClientException;
import org.ylzl.eden.spring.framework.error.ServerException;
import org.ylzl.eden.spring.framework.error.ThirdServiceException;

import java.util.Arrays;

/**
 * 日志切面
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Slf4j
@Aspect
public class CatchLogAspect {

	public static final String UNKNOWN_CAUSE = "UNKNOWN_CAUSE";

	public static final String UNKNOWN_EXCEPTION = "UnknownException";

	public static final String ERROR_LOG = "{} in {}() with cause = '{}' and exception = '{}'";

	public static final String ERROR_LOG_WITH_ARGS = "{} in {}() with argument[s] = {} " +
		"and cause = '{}' and exception = '{}'";

	public static final String ENTER_LOG = "Enter: {}() with argument[s] = {}";

	public static final String EXIT_LOG = "Exit: {}() with result = {}";

	@Pointcut("@within(org.ylzl.eden.cola.catchlog.CatchLog) && execution(public * *(..))")
	public void pointcut() {
	}

	@Around("pointcut()")
	public Object around(ProceedingJoinPoint joinPoint) {
		if (log.isDebugEnabled()) {
			log.debug(ENTER_LOG, joinPoint.getSignature().getDeclaringTypeName(), Arrays.toString(joinPoint.getArgs()));
		}
		Object response = null;
		try {
			response = joinPoint.proceed();
		} catch (Throwable e) {
			response = handleException(joinPoint, e);
		} finally {
			if (log.isDebugEnabled()) {
				log.debug(EXIT_LOG, joinPoint.getSignature().getDeclaringTypeName(), response);
			}
		}
		return response;
	}

	private Object handleException(ProceedingJoinPoint joinPoint, Throwable e) {
		MethodSignature ms = (MethodSignature) joinPoint.getSignature();
		Class<?> returnType = ms.getReturnType();

		String errorTag;
		BaseException baseException;
		if (e instanceof ClientException || e instanceof ServerException ||
			e instanceof ThirdServiceException) {
			errorTag = e.getClass().getSimpleName();
			baseException = (BaseException) e;
		} else {
			errorTag = UNKNOWN_EXCEPTION;
			baseException = new BaseException("SYS-ERROR-500", e.getMessage());
		}

		String cause = e.getCause() != null ? e.getCause().toString() : UNKNOWN_CAUSE;
		if (log.isDebugEnabled()) {
			log.error(ERROR_LOG, errorTag, joinPoint.getSignature().getDeclaringTypeName(),
				cause, e.getMessage(), e);
		} else {
			// 记录抛出异常信息的参数
			log.error(ERROR_LOG_WITH_ARGS, errorTag, joinPoint.getSignature().getDeclaringTypeName(),
				Arrays.toString(joinPoint.getArgs()), cause, e.getMessage(), e);
		}
		return CatchLogHandler.wrap(returnType, baseException);
	}
}
