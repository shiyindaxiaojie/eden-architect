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
package org.ylzl.eden.spring.boot.commons.env;

import lombok.experimental.UtilityClass;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 字符集常量
 *
 * @author gyl
 * @since 1.0.0
 */
@UtilityClass
public class CharsetConstants {

  public static final Charset GBK = Charset.forName("GBK");

  public static final Charset ISO_8859_1 = StandardCharsets.ISO_8859_1;

  public static final Charset US_ASCII = StandardCharsets.US_ASCII;

  public static final Charset UTF_8 = StandardCharsets.UTF_8;

  public static final Charset UTF_16 = StandardCharsets.UTF_16;

  public static final Charset UTF_16BE = StandardCharsets.UTF_16BE;

  public static final Charset UTF_16LE = StandardCharsets.UTF_16LE;

  public static final String GBK_NAME = GBK.name();

  public static final String ISO_8859_1_NAME = ISO_8859_1.name();

  public static final String US_ASCII_NAME = US_ASCII.name();

  public static final String UTF_8_NAME = UTF_8.name();

  public static final String UTF_16_NAME = UTF_16.name();

  public static final String UTF_16BE_NAME = UTF_16BE.name();

  public static final String UTF_16LE_NAME = UTF_16LE.name();
}
