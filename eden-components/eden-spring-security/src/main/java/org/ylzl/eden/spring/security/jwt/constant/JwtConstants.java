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

package org.ylzl.eden.spring.security.jwt.constant;

import io.jsonwebtoken.Claims;
import lombok.experimental.UtilityClass;

/**
 * JWT 常量定义
 *
 * @author gyl
 * @since 2.4.x
 */
@UtilityClass
public final class JwtConstants {

  public static final String AUDIENCE = Claims.AUDIENCE;

  public static final String EXPIRATION = Claims.EXPIRATION;

  public static final String ID = Claims.ID;

  public static final String ISSUED_AT = Claims.ISSUED_AT;

  public static final String ISSUER = Claims.ISSUER;

  public static final String NOT_BEFORE = Claims.NOT_BEFORE;

  public static final String SUBJECT = Claims.SUBJECT;

  public static final String CLAIM_KEY = "auth";

  public static final String ENDPOINT_TOKEN = "/auth";
}
