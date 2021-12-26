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

import org.ylzl.eden.spring.framework.web.rest.vm.ParameterizedErrorVM;

import java.util.HashMap;
import java.util.Map;

/**
 * 自定义参数异常
 *
 * @author gyl
 * @since 1.0.0
 */
public class CustomParameterizedException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  private static final String PARAM = "param";

  private final String message;

  private final String description;

  private final Map<String, String> paramMap = new HashMap<>();

  public CustomParameterizedException(String message, String... params) {
    super(message);
    this.message = message;
    this.description = message;
    if (params != null && params.length > 0) {
      for (int i = 0; i < params.length; i++) {
        paramMap.put(PARAM + i, params[i]);
      }
    }
  }

  public CustomParameterizedException(String message, Map<String, String> paramMap) {
    super(message);
    this.message = message;
    this.description = message;
    this.paramMap.putAll(paramMap);
  }

  public CustomParameterizedException(
      String message, String description, Map<String, String> paramMap) {
    super(message);
    this.message = message;
    this.description = description;
    this.paramMap.putAll(paramMap);
  }

  public ParameterizedErrorVM getErrorVM() {
    return ParameterizedErrorVM.builder()
        .message(message)
        .description(description)
        .paramMap(paramMap)
        .build();
  }
}
