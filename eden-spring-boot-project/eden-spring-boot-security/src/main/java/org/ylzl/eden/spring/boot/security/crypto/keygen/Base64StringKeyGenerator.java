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

package org.ylzl.eden.spring.boot.security.crypto.keygen;

import org.springframework.security.crypto.keygen.BytesKeyGenerator;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.security.crypto.keygen.StringKeyGenerator;
import org.ylzl.eden.spring.boot.commons.codec.binary.Base64;

/**
 * Base 64 字符串密码生成器
 *
 * @author gyl
 * @since 0.0.1
 */
public class Base64StringKeyGenerator implements StringKeyGenerator {

  private static final int DEFAULT_KEY_LENGTH = 32;

  private final BytesKeyGenerator keyGenerator;

  public Base64StringKeyGenerator() {
    this(DEFAULT_KEY_LENGTH);
  }

  public Base64StringKeyGenerator(int keyLength) {
    if (keyLength < DEFAULT_KEY_LENGTH) {
      throw new IllegalArgumentException(
          "keyLength must be greater than or equal to" + DEFAULT_KEY_LENGTH);
    }
    this.keyGenerator = KeyGenerators.secureRandom(keyLength);
  }

  @Override
  public String generateKey() {
    byte[] key = this.keyGenerator.generateKey();
    byte[] base64EncodedKey = Base64.encodeBase64(key);
    return new String(base64EncodedKey);
  }
}
