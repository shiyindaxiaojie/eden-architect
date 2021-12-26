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

package org.ylzl.eden.spring.cloud.feign.authentication;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.annotation.AliasFor;
import org.ylzl.eden.commons.lang.StringConstants;

import java.lang.annotation.*;

/**
 * 已认证的 FeignClient 注解
 *
 * <p>从 Spring Boot 1.X 升级到 2.X
 *
 * <ul>
 *   <li>org.springframework.cloud.netflix.feign.FeignClient 迁移到 {@link FeignClient}
 * </ul>
 *
 * @author gyl
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@FeignClient
public @interface AuthorizedFeignClient {

  @AliasFor(annotation = FeignClient.class, attribute = "name")
  String name() default StringConstants.EMPTY;

  @AliasFor(annotation = FeignClient.class, attribute = "configuration")
  Class<?>[] configuration();
  // default AuthorizedFeignConfiguration.class;

  String url() default StringConstants.EMPTY;

  String qualifier() default StringConstants.EMPTY;

  boolean decode404() default false;

  Class<?> fallback() default void.class;

  String path() default StringConstants.EMPTY;
}
