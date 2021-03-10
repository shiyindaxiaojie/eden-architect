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

/**
 * Profile 常量定义
 *
 * @author gyl
 * @since 1.0.0
 */
@UtilityClass
public final class ProfileConstants {

  /** 开发环境 */
  public static final String SPRING_PROFILE_DEVELOPMENT = "dev";

  /** 生产环境 */
  public static final String SPRING_PROFILE_PRODUCTION = "prod";

  /** 预发布环境 */
  public static final String SPRING_PROFILE_PRRVIEW = "pre";

  /** 测试环境 */
  public static final String SPRING_PROFILE_TEST = "test";

  /** 演示环境 */
  public static final String SPRING_PROFILE_DEMO = "demo";

  /** 云环境 */
  public static final String SPRING_PROFILE_CLOUD = "cloud";

  /** 本地环境 */
  public static final String SPRING_PROFILE_NATIVE = "native";
}
