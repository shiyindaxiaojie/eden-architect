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
package org.ylzl.eden.spring.boot.commons.crypto;

import lombok.experimental.UtilityClass;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

/**
 * 加密解密工具集
 *
 * @author gyl
 * @since 1.0.0
 */
@UtilityClass
public class CryptoUtils {

  public static byte[] cipher(
      byte[] data,
      byte[] secretKey,
      String algorithm,
      String transformation,
      String provider,
      int decryptMode)
      throws Exception {
    Cipher cipher = Cipher.getInstance(transformation, provider);
    Key key = new SecretKeySpec(secretKey, algorithm);
    cipher.init(decryptMode, key);
    return cipher.doFinal(data);
  }

  public static byte[] encrypt(
      byte[] data, byte[] secretKey, String algorithm, String transformation, String provider)
      throws Exception {
    return cipher(data, secretKey, algorithm, transformation, provider, Cipher.ENCRYPT_MODE);
  }

  public static byte[] decrypt(
      byte[] data, byte[] secretKey, String algorithm, String transformation, String provider)
      throws Exception {
    return cipher(data, secretKey, algorithm, transformation, provider, Cipher.DECRYPT_MODE);
  }

  public static byte[] generatorSecretKey(String algorithm, int length) throws Exception {
    KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);
    keyGenerator.init(length);
    SecretKey secretKey = keyGenerator.generateKey();
    return secretKey.getEncoded();
  }
}
