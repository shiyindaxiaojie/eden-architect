/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
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

package org.ylzl.eden.spring.boot.framework.cache;

import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.interceptor.KeyGenerator;
import org.ylzl.eden.spring.boot.commons.lang.ObjectUtils;
import org.ylzl.eden.spring.boot.commons.lang.RandomStringUtils;

import java.lang.reflect.Method;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * 前缀 Key 生成器
 *
 * @author gyl
 * @since 2.0.0
 */
public class PrefixedKeyGenerator implements KeyGenerator {

  private final String prefix;

  public PrefixedKeyGenerator(GitProperties gitProperties, BuildProperties buildProperties) {

    this.prefix = generatePrefix(gitProperties, buildProperties);
  }

  String getPrefix() {
    return this.prefix;
  }

  private String generatePrefix(GitProperties gitProperties, BuildProperties buildProperties) {

    String shortCommitId = null;
    if (Objects.nonNull(gitProperties)) {
      shortCommitId = gitProperties.getShortCommitId();
    }

    Instant time = null;
    String version = null;
    if (Objects.nonNull(buildProperties)) {
      time = buildProperties.getTime();
      version = buildProperties.getVersion();
    }
    Object p =
        ObjectUtils.firstNonNull(
            shortCommitId, time, version, RandomStringUtils.randomAlphanumeric(12));

    if (p instanceof Instant) {
      return DateTimeFormatter.ISO_INSTANT.format((Instant) p);
    }
    return p.toString();
  }

  @Override
  public Object generate(Object target, Method method, Object... params) {
    return new PrefixedSimpleKey(prefix, method.getName(), params);
  }
}
