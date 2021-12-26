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

package org.ylzl.eden.spring.framework.web.rest.errors;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.ylzl.eden.spring.framework.web.rest.vm.ErrorVM;

/**
 * 错误请求
 *
 * @author gyl
 * @since 1.0.0
 */
@Getter
public class BadRequestAlertException extends RuntimeException {

  private static final long serialVersionUID = -4672286153615627725L;

  private final String message;

  private final String description;

  public BadRequestAlertException() {
    super(ErrorEnum.BAD_REQUEST_ALERT.getMessage());
    this.message = ErrorEnum.BAD_REQUEST_ALERT.getMessage();
    this.description = ErrorEnum.BAD_REQUEST_ALERT.getMessage();
  }

  public BadRequestAlertException(String message) {
    super(message);
    this.message = message;
    this.description = message;
  }

  public BadRequestAlertException(String message, String description) {
    super(message);
    this.message = message;
    this.description = description;
  }

  public int getStatusCode() {
    return HttpStatus.BAD_REQUEST.value();
  }

  public ErrorVM getErrorVM() {
    return ErrorVM.builder().message(message).description(description).build();
  }
}
