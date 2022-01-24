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

package org.ylzl.eden.spring.security.oauth2.token;

/**
 * 令牌处理器
 *
 * @author gyl
 * @since 2.4.x
 */
public interface TokenProcessor {

  /**
   * 根据令牌获取签发时间
   *
   * @param tokenValue 令牌值
   * @return 签发时间
   */
  int getIat(String tokenValue);

  /**
   * 根据令牌获取过期时间
   *
   * @param tokenValue 令牌值
   * @return 过期时间
   */
  int getExp(String tokenValue);
}
