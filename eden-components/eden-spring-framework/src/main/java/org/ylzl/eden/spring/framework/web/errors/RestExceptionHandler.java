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

package org.ylzl.eden.spring.framework.web.errors;

import com.alibaba.cola.dto.Response;
import org.apache.commons.lang3.StringUtils;
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

import java.util.List;

/**
 * 异常转换器
 *
 * @author gyl
 * @since 2.4.x
 */
@RestControllerAdvice
public class RestExceptionHandler {

	/**
	 * 处理服务器内部异常
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
			response = Response.buildFailure(ErrorEnum.A0500.getErrCode(), responseStatus.reason());
		} else {
			builder = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
			response = Response.buildFailure(ErrorEnum.A0500.getErrCode(), ErrorEnum.A0500.getErrMessage());
		}
		return builder.body(response);
	}

	/**
	 * 处理参数校验无效异常
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
		return Response.buildFailure(ErrorEnum.A0001.getErrCode(), message);
	}

	/**
	 * 处理不支持的请求方法异常
	 *
	 * @param ex 不支持的请求方法异常
	 * @return
	 */
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
	public Response processMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
		return Response.buildFailure(ErrorEnum.A0001.getErrCode(), "不支持的请求方法");
	}

	/**
	 * 处理客户端错误
	 *
	 * @param ex 异常
	 * @return
	 */
	@ExceptionHandler(BadRequestException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public Response processBadRequestException(BadRequestException ex) {
		return ex.getError();
	}

	/**
	 * 处理请求未认证错误
	 *
	 * @param ex 异常
	 * @return
	 */
	@ExceptionHandler(UnauthorizedException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ResponseBody
	public Response processUnauthorizedException(UnauthorizedException ex) {
		return ex.getError();
	}

	/**
	 * 处理非法访问错误
	 *
	 * @param ex 异常
	 * @return
	 */
	@ExceptionHandler(ForbiddenException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ResponseBody
	public Response processForbiddenException(ForbiddenException ex) {
		return ex.getError();
	}
}
