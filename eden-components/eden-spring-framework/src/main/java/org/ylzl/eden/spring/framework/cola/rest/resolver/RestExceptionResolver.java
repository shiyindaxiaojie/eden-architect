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

package org.ylzl.eden.spring.framework.cola.rest.resolver;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.ylzl.eden.spring.framework.cola.dto.Response;
import org.ylzl.eden.spring.framework.error.ClientException;
import org.ylzl.eden.spring.framework.error.ServerException;
import org.ylzl.eden.spring.framework.error.ThirdServiceException;
import org.ylzl.eden.spring.framework.cola.rest.event.RestExceptionEvent;
import org.ylzl.eden.spring.framework.error.http.BadRequestException;
import org.ylzl.eden.spring.framework.error.http.ForbiddenException;
import org.ylzl.eden.spring.framework.error.http.UnauthorizedException;

import java.util.List;

/**
 * Rest 异常解析器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Slf4j
@RestControllerAdvice
public class RestExceptionResolver implements ApplicationEventPublisherAware {

	private static final String EXCEPTION_HANDLER_CATCH = "ExceptionHandler catch: {}";

	private ApplicationEventPublisher eventPublisher;

	@Override
	public void setApplicationEventPublisher(@NotNull ApplicationEventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	/**
	 * HTTP 500 错误处理
	 *
	 * @param ex 服务器内部异常
	 * @return
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Response> processException(Exception ex) {
		BodyBuilder builder;
		Response response;
		ResponseStatus responseStatus =
			AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class);
		if (responseStatus != null) {
			builder = ResponseEntity.status(responseStatus.value());
			response = Response.buildFailure("B0001", responseStatus.reason());
		} else {
			builder = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
			response = Response.buildFailure("B0001");
		}
		this.process(ex, response);
		return builder.body(response);
	}

	/**
	 * HTTP 400 错误处理
	 *
	 * @param ex 方法参数校验无效异常
	 * @return
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public Response processValidationException(MethodArgumentNotValidException ex) {
		BindingResult result = ex.getBindingResult();
		List<FieldError> fieldErrors = result.getFieldErrors();
		String message = StringUtils.join(fieldErrors.toArray(), ",");
		Response response = Response.buildFailure("A0001", message);
		this.process(ex, response);
		return response;
	}

	/**
	 * HTTP 405 错误处理
	 *
	 * @param ex 不支持的请求方法异常
	 * @return
	 */
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
	@ResponseBody
	public Response processMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
		Response response = Response.buildFailure("A0001", "不支持的请求方法");
		this.process(ex, response);
		return response;
	}

	/**
	 * HTTP 400 错误处理
	 *
	 * @param ex 异常
	 * @return
	 */
	@ExceptionHandler(BadRequestException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public Response processBadRequestException(BadRequestException ex) {
		Response response = Response.buildFailure(ex.getErrCode(), ex.getErrMessage(), ex.getParams());
		this.process(ex, response);
		return response;
	}

	/**
	 * HTTP 401 错误处理
	 *
	 * @param ex 异常
	 * @return
	 */
	@ExceptionHandler(UnauthorizedException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ResponseBody
	public Response processUnauthorizedException(UnauthorizedException ex) {
		Response response = Response.buildFailure(ex.getErrCode(), ex.getErrMessage(), ex.getParams());
		this.process(ex, response);
		return response;
	}

	/**
	 * HTTP 403 错误处理
	 *
	 * @param ex 异常
	 * @return
	 */
	@ExceptionHandler(ForbiddenException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ResponseBody
	public Response processForbiddenException(ForbiddenException ex) {
		Response response = Response.buildFailure(ex.getErrCode(), ex.getErrMessage(), ex.getParams());
		this.process(ex, response);
		return response;
	}

	/**
	 * 客户端错误处理
	 *
	 * @param ex 异常
	 * @return
	 */
	@ExceptionHandler(ClientException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public Response processClientException(ClientException ex) {
		Response response = Response.buildFailure(ex.getErrCode(), ex.getErrMessage());
		this.process(ex, response);
		return response;
	}

	/**
	 * 服务端错误处理
	 *
	 * @param ex 异常
	 * @return
	 */
	@ExceptionHandler(ServerException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public Response processServerException(ServerException ex) {
		Response response = Response.buildFailure(ex.getErrCode(), ex.getErrMessage());
		this.process(ex, response);
		return response;
	}

	/**
	 * 第三方服务错误处理
	 *
	 * @param ex 异常
	 * @return
	 */
	@ExceptionHandler(ThirdServiceException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public Response processThirdServiceException(ThirdServiceException ex) {
		Response response = Response.buildFailure(ex.getErrCode(), ex.getErrMessage());
		this.process(ex, response);
		return response;
	}

	/**
	 * 后置处理
	 *
	 * @param response
	 */
	private void process(Throwable e, Response response) {
		// 打印错误
		log.error(EXCEPTION_HANDLER_CATCH, e.getMessage(), e);
		// 发布事件
		eventPublisher.publishEvent(RestExceptionEvent.builder().response(response).build());
	}
}
