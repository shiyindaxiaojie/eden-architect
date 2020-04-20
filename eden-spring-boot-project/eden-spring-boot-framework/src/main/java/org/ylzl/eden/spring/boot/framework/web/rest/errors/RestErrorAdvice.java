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

package org.ylzl.eden.spring.boot.framework.web.rest.errors;

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
import org.ylzl.eden.spring.boot.framework.web.rest.vm.ErrorVM;
import org.ylzl.eden.spring.boot.framework.web.rest.vm.ParameterizedErrorVM;

import java.util.List;

/**
 * 异常转换器
 *
 * @author gyl
 * @since 1.0.0
 */
@RestControllerAdvice
public class RestErrorAdvice {

    /**
     * 处理参数校验无效异常
     *
     * @param ex 方法参数校验无效异常
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorVM processValidationException(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        ErrorVM errorVM = ErrorVM.builder().message(ErrorConstants.ERR_VALIDATION).build();
        for (FieldError fieldError : fieldErrors) {
            errorVM.add(fieldError.getObjectName(), fieldError.getField(), fieldError.getCode());
        }
        return errorVM;
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
    public ErrorVM processMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        return ErrorVM.builder().message(ErrorConstants.ERR_METHOD_NOT_SUPPORTED).build();
    }

    /**
     * 处理服务器内部异常
     *
     * @param ex 服务器内部异常
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorVM> processException(Exception ex) {
        BodyBuilder builder;
        ErrorVM errorVM;
        ResponseStatus responseStatus = AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class);
        if (responseStatus != null) {
            builder = ResponseEntity.status(responseStatus.value());
            errorVM = ErrorVM.builder().message(responseStatus.reason()).description(responseStatus.toString()).build();
        } else {
            builder = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
            errorVM = ErrorVM.builder().message(ErrorConstants.ERR_INTERNAL_SERVER_ERROR)
                .description(ex.getMessage()).build();
        }
        return builder.body(errorVM);
    }

    /**
     * 处理自定义参数异常
     *
     * @param ex 自定义参数异常
     * @return
     */
    @ExceptionHandler(CustomParameterizedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ParameterizedErrorVM processParameterizedValidationException(CustomParameterizedException ex) {
        return ex.getErrorVM();
    }

    /**
     * 处理错误请求异常
     *
     * @param ex 错误请求异常
     * @return
     */
    @ExceptionHandler(BadRequestAlertException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorVM processBadRequestAlertException(BadRequestAlertException ex) {
        return ex.getErrorVM();
    }

    /**
     * 处理实体获取为空异常
     *
     * @param ex 实体获取为空异常
     * @return
     */
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorVM processEntityNotFoundException(EntityNotFoundException ex) {
        return ex.getErrorVM();
    }

    /**
     * 处理无效主键异常
     *
     * @param ex 无效主键异常
     * @return
     */
    @ExceptionHandler(InvalidPrimaryKeyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorVM processInvalidPrimaryKeyException(InvalidPrimaryKeyException ex) {
        return ex.getErrorVM();
    }

    /**
     * 处理非法访问异常
     *
     * @param ex 非法访问异常
     * @return
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ErrorVM processAccessDeniedException(AccessDeniedException ex) {
        return ex.getErrorVM();
    }

    /**
     * 处理请求未认证异常
     *
     * @param ex 请求未认证异常
     * @return
     */
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ErrorVM processUnauthorizedException(UnauthorizedException ex) {
        return ex.getErrorVM();
    }
}
