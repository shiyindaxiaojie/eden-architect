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
package org.ylzl.eden.commons.crypto;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.ylzl.eden.commons.codec.binary.Base64;

/**
 * AES 工具集
 *
 * @author gyl
 * @since 2.4.x
 */
@UtilityClass
public class AESUtils {

  private static final String ALGORITHM = "AES";

  private static final String CIPHER_RROVIDER = "BC";

  private static final String DEFAULT_TRANSFORMATION = "AES/ECB/PKCS5Padding";

  public static byte[] encrypt(@NonNull byte[] data, @NonNull byte[] secretKey) throws Exception {
    return CryptoUtils.encrypt(data, secretKey, ALGORITHM, DEFAULT_TRANSFORMATION, CIPHER_RROVIDER);
  }

  public static byte[] encrypt(@NonNull String data, @NonNull byte[] secretKey) throws Exception {
    return encrypt(data.getBytes(), secretKey);
  }

  public static byte[] encrypt(@NonNull String data, @NonNull String secretKey) throws Exception {
    return encrypt(data.getBytes(), Base64.decodeBase64(secretKey));
  }

  public static String encryptToBase64String(@NonNull String data, @NonNull String secretKey)
      throws Exception {
    return Base64.encodeBase64String(encrypt(data, secretKey));
  }

  public static byte[] decrypt(@NonNull byte[] data, @NonNull byte[] secretKey) throws Exception {
    return CryptoUtils.decrypt(data, secretKey, ALGORITHM, DEFAULT_TRANSFORMATION, CIPHER_RROVIDER);
  }

  public static byte[] decrypt(@NonNull String data, @NonNull String secretKey) throws Exception {
    byte[] byteData;
    if (Base64.isBase64(data)) {
      byteData = Base64.decodeBase64(data);
    } else {
      byteData = data.getBytes();
    }
    return decrypt(byteData, Base64.decodeBase64(secretKey));
  }

  public static String decryptToString(@NonNull String data, @NonNull String secretKey)
      throws Exception {
    return new String(decrypt(data, secretKey));
  }

  public static String generatorSecretKey(int length) throws Exception {
    return Base64.encodeBase64String(CryptoUtils.generatorSecretKey(ALGORITHM, length));
  }

  public static String generatorSecretKey() throws Exception {
    return generatorSecretKey(256);
  }
}
