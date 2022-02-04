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

package org.ylzl.eden.spring.framework.cola.catchlog.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.ylzl.eden.spring.framework.cola.catchlog.handler.CatchLogHandler;
import org.ylzl.eden.spring.framework.cola.exception.BaseException;
import org.ylzl.eden.spring.framework.cola.exception.ClientException;
import org.ylzl.eden.spring.framework.cola.exception.ServerException;
import org.ylzl.eden.spring.framework.cola.exception.ThirdServiceException;

import java.util.Arrays;

/**
 * 日志切面
 *
 * @author gyl
 * @since 2.4.x
 */
@Slf4j
@Component
@Aspect
public class CatchLogAspect {

	public static final String UNKNOWN_ERROR = "UNKNOWN_ERROR";

	@Pointcut("@within(org.ylzl.eden.spring.framework.cola.catchlog.annotation.CatchLog) && execution(public * *(..))")
	public void catchLogPointcut() {
	}

	@Around("catchLogPointcut()")
	public Object logAround(ProceedingJoinPoint joinPoint) {
		if (log.isDebugEnabled()) {
			log.debug("Enter: {}() with argument[s] = {}", joinPoint.getSignature().getDeclaringTypeName(), Arrays.toString(joinPoint.getArgs()));
		}
		Object response = null;
		try {
			response = joinPoint.proceed();
		}
		catch (Throwable e){
			response = handleException(joinPoint, e);
		}
		finally {
			if (log.isDebugEnabled()) {
				log.debug("Exit: {}() with result = {}",
					joinPoint.getSignature().getDeclaringTypeName(), response);
			}
		}
		return response;
	}

	private Object handleException(ProceedingJoinPoint joinPoint, Throwable e) {
		MethodSignature ms = (MethodSignature) joinPoint.getSignature();
		Class<?> returnType = ms.getReturnType();

		if (e instanceof ClientException || e instanceof ServerException ||
			e instanceof ThirdServiceException) {
			log.error("{} in {}() with cause = '{}' and exception = '{}'", e.getClass().getSimpleName(),
				joinPoint.getSignature().getDeclaringTypeName(), e.getCause() != null ? e.getCause() : "NULL", e.getMessage(), e);
			return CatchLogHandler.response(returnType, (BaseException) e);
		}

		BaseException baseException = new BaseException(UNKNOWN_ERROR, e.getMessage(),
			HttpStatus.INTERNAL_SERVER_ERROR.value());
		return CatchLogHandler.response(returnType, baseException);
	}
}
