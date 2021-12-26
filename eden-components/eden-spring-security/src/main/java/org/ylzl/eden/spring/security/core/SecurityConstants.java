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

package org.ylzl.eden.spring.security.core;

import lombok.experimental.UtilityClass;
import org.ylzl.eden.spring.framework.core.constant.SpringFrameworkConstants;

/**
 * Security 常量定义
 *
 * @author gyl
 * @since 1.0.0
 */
@UtilityClass
public final class SecurityConstants {

  /** 自定义属性前缀 */
  public static final String PROP_PREFIX = SpringFrameworkConstants.PROP_EDEN_PREFIX + ".security";

  // 角色

  /** 管理员 */
  public static final String ADMIN = "ADMIN";

  /** 注册用户 */
  public static final String ROLE_USER = "ROLE_USER";

  /** 匿名用户 */
  public static final String ROLE_ANONYMOUS = "ROLE_ANONYMOUS";

  // 认证参数

  /** Bearer 认证类型 */
  public static final String BEARER_TOKEN = "Bearer ";

  /** Basic 认证类型 */
  public static final String BASIC_AUTH = "Basic ";
}
