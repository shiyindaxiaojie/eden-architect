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

import lombok.experimental.UtilityClass;

/**
 * 错误常量定义
 *
 * @author gyl
 * @since 1.0.0
 */
@UtilityClass
public final class ErrorConstants {

  public static final String ERR_INTERNAL_SERVER_ERROR = "服务器内部错误";

  public static final String ERR_CONCURRENCY_FAILURE = "并发失败";

  public static final String ERR_INVALID_CREDENTIALS = "无效凭据";

  public static final String ERR_BAD_REQUEST_ALERT = "错误请求";

  public static final String ERR_ACCESS_DENIED = "非法访问";

  public static final String ERR_UNAUTHORIZED = "请求未认证或者认证失败";

  public static final String ERR_METHOD_NOT_SUPPORTED = "不支持的方法";

  public static final String ERR_VALIDATION = "校验失败";

  public static final String ERR_ENTITY_NOT_FOUND = "实体获取为空";

  public static final String ERR_INVALID_PRIMARY_KEY = "无效主键";
}
