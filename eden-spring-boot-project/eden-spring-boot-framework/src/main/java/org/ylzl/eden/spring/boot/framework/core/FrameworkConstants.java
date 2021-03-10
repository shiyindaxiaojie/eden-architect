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

package org.ylzl.eden.spring.boot.framework.core;

import lombok.experimental.UtilityClass;
import org.ylzl.eden.spring.boot.commons.env.CharsetConstants;

/**
 * 框架常量定义
 *
 * @author gyl
 * @since 1.0.0
 */
@UtilityClass
public final class FrameworkConstants {

  /** 默认编码 */
  public static final String DEFAULT_ENCODING = CharsetConstants.UTF_8_NAME;

  /** 默认语言 */
  public static final String DEFAULT_LANGUAGE = "zh-cn";

  /** 默认字体 */
  public static final String DEFAULT_FONT_NAME = "simsun";

  /** 系统用户 */
  public static final String SYSTEM = "system";

  /** 匿名用户 */
  public static final String ANONYMOUS_USER = "anonymoususer";

  /**
   * Spring Boot 读取应用程序名称
   *
   * @see org.springframework.boot.context.ContextIdApplicationContextInitializer
   */
  public static final String NAME_PATTERN =
      "${spring.application.name:${vcap.application.name:${spring.config.name:application}}}";

  /** Spring Boot 读取应用程序启动端口 */
  public static final String PORT_PATTERN = "${server.port}";

  /**
   * Spring Boot 读取应用程序索引
   *
   * @see org.springframework.boot.context.ContextIdApplicationContextInitializer
   */
  public static final String INDEX_PATTERN =
      "${vcap.application.instance_index:${spring.application.index:${server.port:${PORT:null}}}}";

  /** Spring 属性前缀 */
  public static final String PROP_SPRING_PREFIX = "spring";

  /** Eden 属性前缀 */
  public static final String PROP_EDEN_PREFIX = "eden";

  /** 当前属性前缀 */
  public static final String PROP_PREFIX = PROP_EDEN_PREFIX + ".framework";
}
