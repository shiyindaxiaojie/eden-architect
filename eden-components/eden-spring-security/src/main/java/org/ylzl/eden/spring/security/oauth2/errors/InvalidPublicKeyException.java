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
package org.ylzl.eden.spring.security.oauth2.errors;

import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;

/**
 * 无效的公钥
 *
 * @author gyl
 * @since 2.4.x
 */
public class InvalidPublicKeyException extends InvalidTokenException {

  public static final String ERR_INVALID_PUBLIC_KEY = "无效的公钥";

  public InvalidPublicKeyException() {
    super(ERR_INVALID_PUBLIC_KEY);
  }

  public InvalidPublicKeyException(String message) {
    super(message);
  }

  public InvalidPublicKeyException(String message, Throwable e) {
    super(message, e);
  }
}
