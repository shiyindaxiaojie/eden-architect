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

package org.ylzl.eden.spring.framework.web.rest.resolver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.ylzl.eden.commons.lang.StringUtils;
import org.ylzl.eden.spring.framework.error.BaseException;
import org.ylzl.eden.spring.framework.error.ClientException;
import org.ylzl.eden.spring.framework.error.ServerException;
import org.ylzl.eden.spring.framework.error.ThirdServiceException;
import org.ylzl.eden.spring.framework.error.http.BadRequestException;
import org.ylzl.eden.spring.framework.error.http.ForbiddenException;
import org.ylzl.eden.spring.framework.error.http.UnauthorizedException;
import org.ylzl.eden.spring.framework.web.extension.ResponseBuilder;

import java.util.List;

/**
 * Rest 异常解析器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Slf4j
@RestControllerAdvice
public class RestExceptionResolver {

	private static final String EXCEPTION_HANDLER_CATCH = "ExceptionHandler catch error: {}";

	/**
	 * HTTP 500 错误处理
	 *
	 * @param ex 服务器内部异常
	 * @return
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> resolveException(Exception ex) {
		BodyBuilder builder;
		Object response;
		ResponseStatus responseStatus = AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class);
		if (responseStatus != null) {
			builder = ResponseEntity.status(responseStatus.value());
			response = builder().buildFailure("SYS-ERROR-500", responseStatus.reason());
		} else {
			builder = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
			response = builder().buildFailure("SYS-ERROR-500", ex.getMessage());
		}
		this.postProcess(ex);
		return builder.body(response);
	}

	/**
	 * HTTP 400 错误处理
	 *
	 * @param ex 方法参数校验无效异常
	 * @return
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> resolveValidationException(MethodArgumentNotValidException ex) {
		BindingResult result = ex.getBindingResult();
		List<FieldError> fieldErrors = result.getFieldErrors();
		String message = StringUtils.join(fieldErrors.toArray(), ",");
		Object response = builder().buildFailure("REQ-ERROR-400", message);
		return this.buildResponseEntity(HttpStatus.BAD_REQUEST, response);
	}

	/**
	 * HTTP 405 错误处理
	 *
	 * @param ex 不支持的请求方法异常
	 * @return
	 */
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<?> resolveMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
		Object response = builder().buildFailure("REQ-ERROR-400", "不支持的请求方法");
		return this.buildResponseEntity(HttpStatus.METHOD_NOT_ALLOWED, response);
	}

	/**
	 * HTTP 400 错误处理
	 *
	 * @param ex 异常
	 * @return
	 */
	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<?> resolveBadRequestException(BadRequestException ex) {
		return this.buildResponseEntity(HttpStatus.BAD_REQUEST, ex);
	}

	/**
	 * HTTP 401 错误处理
	 *
	 * @param ex 异常
	 * @return
	 */
	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<?> resolveUnauthorizedException(UnauthorizedException ex) {
		return this.buildResponseEntity(HttpStatus.UNAUTHORIZED, ex);
	}

	/**
	 * HTTP 403 错误处理
	 *
	 * @param ex 异常
	 * @return
	 */
	@ExceptionHandler(ForbiddenException.class)
	public ResponseEntity<?> resolveForbiddenException(ForbiddenException ex) {
		return this.buildResponseEntity(HttpStatus.FORBIDDEN, ex);
	}

	/**
	 * 客户端错误处理
	 *
	 * @param ex 异常
	 * @return
	 */
	@ExceptionHandler(ClientException.class)
	public ResponseEntity<?> resolveClientException(ClientException ex) {
		return this.buildResponseEntity(HttpStatus.BAD_REQUEST, ex);
	}

	/**
	 * 服务端错误处理
	 *
	 * @param ex 异常
	 * @return
	 */
	@ExceptionHandler(ServerException.class)
	public ResponseEntity<?> resolveServerException(ServerException ex) {
		this.postProcess(ex);
		return this.buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, ex);
	}

	/**
	 * 第三方服务错误处理
	 *
	 * @param ex 异常
	 * @return
	 */
	@ExceptionHandler(ThirdServiceException.class)
	public ResponseEntity<?> resolveThirdServiceException(ThirdServiceException ex) {
		this.postProcess(ex);
		return this.buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, ex);
	}

	private ResponseBuilder<?> builder() {
		return ResponseBuilder.builder();
	}

	private ResponseEntity<?> buildResponseEntity(HttpStatus httpStatus, Object response) {
		BodyBuilder builder = ResponseEntity.status(httpStatus);
		return builder.body(response);
	}

	private ResponseEntity<?> buildResponseEntity(HttpStatus httpStatus, BaseException ex) {
		Object response = builder().buildFailure(ex.getErrCode(), ex.getErrMessage(), ex.getParams());
		return this.buildResponseEntity(httpStatus, response);
	}

	private void postProcess(Throwable e) {
		log.error(EXCEPTION_HANDLER_CATCH, e.getMessage(), e);

	}
}
