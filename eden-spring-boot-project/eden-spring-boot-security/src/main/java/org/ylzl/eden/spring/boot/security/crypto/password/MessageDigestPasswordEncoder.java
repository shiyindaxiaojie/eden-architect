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

import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.crypto.codec.Utf8;
import org.springframework.security.crypto.keygen.StringKeyGenerator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.ylzl.eden.spring.boot.commons.codec.binary.Base64;
import org.ylzl.eden.spring.boot.security.crypto.keygen.Base64StringKeyGenerator;

/**
 * 消息摘要密码编码器
 *
 * @author gyl
 * @since 1.0.0
 */
public class MessageDigestPasswordEncoder implements PasswordEncoder {

  private static final String PREFIX = "{";

  private static final String SUFFIX = "}";

  private StringKeyGenerator saltGenerator = new Base64StringKeyGenerator();

  private boolean encodeHashAsBase64;

  private Digester digester;

  public MessageDigestPasswordEncoder(String algorithm) {
    this.digester = new Digester(algorithm, 1);
  }

  public void setEncodeHashAsBase64(boolean encodeHashAsBase64) {
    this.encodeHashAsBase64 = encodeHashAsBase64;
  }

  @Override
  public String encode(CharSequence rawPassword) {
    String salt = PREFIX + this.saltGenerator.generateKey() + SUFFIX;
    return digest(salt, rawPassword);
  }

  @Override
  public boolean matches(CharSequence rawPassword, String encodedPassword) {
    String salt = extractSalt(encodedPassword);
    String rawPasswordEncoded = digest(salt, rawPassword);
    return PasswordEncoderUtils.equals(encodedPassword.toString(), rawPasswordEncoded);
  }

  private String digest(String salt, CharSequence rawPassword) {
    String saltedPassword = rawPassword + salt;

    byte[] digest = this.digester.digest(Utf8.encode(saltedPassword));
    String encoded = encode(digest);
    return salt + encoded;
  }

  private String encode(byte[] digest) {
    if (this.encodeHashAsBase64) {
      return Utf8.decode(Base64.encodeBase64(digest));
    } else {
      return new String(Hex.encode(digest));
    }
  }

  public void setIterations(int iterations) {
    this.digester.setIterations(iterations);
  }

  private String extractSalt(String prefixEncodedPassword) {
    int start = prefixEncodedPassword.indexOf(PREFIX);
    if (start != 0) {
      return "";
    }
    int end = prefixEncodedPassword.indexOf(SUFFIX, start);
    if (end < 0) {
      return "";
    }
    return prefixEncodedPassword.substring(start, end + 1);
  }
}
