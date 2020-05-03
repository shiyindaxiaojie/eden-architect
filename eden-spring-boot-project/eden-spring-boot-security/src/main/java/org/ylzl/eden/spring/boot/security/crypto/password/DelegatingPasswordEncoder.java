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

package org.ylzl.eden.spring.boot.security.crypto.password;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Map;

/**
 * 密码编码代理类
 *
 * @author gyl
 * @since 0.0.1
 */
public class DelegatingPasswordEncoder implements PasswordEncoder {

  private static final String PREFIX = "{";

  private static final String SUFFIX = "}";

  private final String idForEncode;

  private final PasswordEncoder passwordEncoderForEncode;

  private final Map<String, PasswordEncoder> idToPasswordEncoder;

  private PasswordEncoder defaultPasswordEncoderForMatches = new UnmappedIdPasswordEncoder();

  public DelegatingPasswordEncoder(
      String idForEncode, Map<String, PasswordEncoder> idToPasswordEncoder) {
    if (idForEncode == null) {
      throw new IllegalArgumentException("idForEncode cannot be null");
    }
    if (!idToPasswordEncoder.containsKey(idForEncode)) {
      throw new IllegalArgumentException(
          "idForEncode "
              + idForEncode
              + "is not found in idToPasswordEncoder "
              + idToPasswordEncoder);
    }
    for (String id : idToPasswordEncoder.keySet()) {
      if (id == null) {
        continue;
      }
      if (id.contains(PREFIX)) {
        throw new IllegalArgumentException("id " + id + " cannot contain " + PREFIX);
      }
      if (id.contains(SUFFIX)) {
        throw new IllegalArgumentException("id " + id + " cannot contain " + SUFFIX);
      }
    }
    this.idForEncode = idForEncode;
    this.passwordEncoderForEncode = idToPasswordEncoder.get(idForEncode);
    this.idToPasswordEncoder = new HashMap<>(idToPasswordEncoder);
  }

  public void setDefaultPasswordEncoderForMatches(
      PasswordEncoder defaultPasswordEncoderForMatches) {
    if (defaultPasswordEncoderForMatches == null) {
      throw new IllegalArgumentException("defaultPasswordEncoderForMatches cannot be null");
    }
    this.defaultPasswordEncoderForMatches = defaultPasswordEncoderForMatches;
  }

  @Override
  public String encode(CharSequence rawPassword) {
    return PREFIX + this.idForEncode + SUFFIX + this.passwordEncoderForEncode.encode(rawPassword);
  }

  @Override
  public boolean matches(CharSequence rawPassword, String prefixEncodedPassword) {
    if (rawPassword == null && prefixEncodedPassword == null) {
      return true;
    }
    String id = extractId(prefixEncodedPassword);
    PasswordEncoder delegate = this.idToPasswordEncoder.get(id);
    if (delegate == null) {
      return this.defaultPasswordEncoderForMatches.matches(rawPassword, prefixEncodedPassword);
    }
    String encodedPassword = extractEncodedPassword(prefixEncodedPassword);
    return delegate.matches(rawPassword, encodedPassword);
  }

  private String extractId(String prefixEncodedPassword) {
    if (prefixEncodedPassword == null) {
      return null;
    }
    int start = prefixEncodedPassword.indexOf(PREFIX);
    if (start != 0) {
      return null;
    }
    int end = prefixEncodedPassword.indexOf(SUFFIX, start);
    if (end < 0) {
      return null;
    }
    return prefixEncodedPassword.substring(start + 1, end);
  }

  private String extractEncodedPassword(String prefixEncodedPassword) {
    int start = prefixEncodedPassword.indexOf(SUFFIX);
    return prefixEncodedPassword.substring(start + 1);
  }

  private class UnmappedIdPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
      throw new UnsupportedOperationException("encode is not supported");
    }

    @Override
    public boolean matches(CharSequence rawPassword, String prefixEncodedPassword) {
      String id = extractId(prefixEncodedPassword);
      throw new IllegalArgumentException(
          "There is no PasswordEncoder mapped for the id \"" + id + "\"");
    }
  }
}
