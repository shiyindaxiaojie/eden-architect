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

package org.ylzl.eden.spring.boot.security.crypto;

import lombok.experimental.UtilityClass;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.ylzl.eden.spring.boot.security.crypto.password.DelegatingPasswordEncoder;
import org.ylzl.eden.spring.boot.security.crypto.password.MessageDigestPasswordEncoder;

import java.util.HashMap;
import java.util.Map;

/**
 * 密码编码器工厂
 *
 * @author gyl
 * @since 1.0.0
 */
@UtilityClass
public final class PasswordEncoderFactories {

  public static final String KEY_BCRYPT = "bcrypt";

  public static final String KEY_MD5 = "MD5";

  public static final String KEY_NO_OP = "noop";

  public static final String KEY_PBKDF2 = "pbkdf2";

  public static final String KEY_SCRYPT = "scrypt";

  public static final String KEY_SHA_1 = "SHA-1";

  public static final String KEY_SHA_256 = "SHA-256";

  public static final String KEY_SHA256 = "sha256";

  public static PasswordEncoder createDelegatingPasswordEncoder() {
    Map<String, PasswordEncoder> encoders = new HashMap<>();
    encoders.put(KEY_BCRYPT, new BCryptPasswordEncoder());
    encoders.put(KEY_MD5, new MessageDigestPasswordEncoder(KEY_MD5));
    encoders.put(KEY_NO_OP, NoOpPasswordEncoder.getInstance());
    encoders.put(KEY_PBKDF2, new Pbkdf2PasswordEncoder());
    encoders.put(KEY_SCRYPT, new SCryptPasswordEncoder());
    encoders.put(KEY_SHA_1, new MessageDigestPasswordEncoder(KEY_SHA_1));
    encoders.put(KEY_SHA_256, new MessageDigestPasswordEncoder(KEY_SHA_256));
    encoders.put(KEY_SHA256, new StandardPasswordEncoder());
    return new DelegatingPasswordEncoder(KEY_BCRYPT, encoders);
  }
}
