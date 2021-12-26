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
package org.ylzl.eden.commons.regex;

import lombok.experimental.UtilityClass;

/**
 * 正则表达式
 *
 * @author gyl
 * @since 1.0.0
 */
@UtilityClass
public class RegexPattern {

  /** 空白行 */
  public static final String REGEX_BLANK_LINE = "\\n\\s*\\r";

  /** 日期 */
  public static final String REGEX_DATE =
      "^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|"
          + "(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|"
          + "(?:0[48]|[2468][048]|[13579][26])00)-02-29)$";

  /** 双字节字符（包括汉字） */
  public static final String REGEX_DOUBLE_BYTE_CHAR = "[^\\x00-\\xff]";

  /** 邮箱 */
  public static final String REGEX_EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";

  /** 浮点数 */
  public static final String REGEX_FLOAT = "^-?[1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*$";

  /** 负浮点数 */
  public static final String REGEX_FLOAT_NEGATIVE = "^-[1-9]\\d*\\.\\d*|-0\\.\\d*[1-9]\\d*$";

  /** 正浮点数 */
  public static final String REGEX_FLOAT_POSITIVE = "^[1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*$";

  /** 身份证号码（15 位） */
  public static final String REGEX_ID_CARD15 =
      "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$";

  /** 身份证号码（18 位） */
  public static final String REGEX_ID_CARD18 =
      "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}" + "([0-9Xx])$";

  /** 整数 */
  public static final String REGEX_INTEGER = "^-?[1-9]\\d*$";

  /** 负整数 */
  public static final String REGEX_INTEGER_NEGATIVE = "^-[1-9]\\d*$";

  /** 非负整数 */
  public static final String REGEX_INTEGER_NEGATIVE_REVERSE = "^[1-9]\\d*|0$";

  /** 正整数 */
  public static final String REGEX_INTEGER_POSITIVE = "^[1-9]\\d*$";

  /** IP 地址 */
  public static final String REGEX_IP =
      "((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)";

  /** 非正整数 */
  public static final String REGEX_INTEGER_POSITIVE_REVERSE = "^-[1-9]\\d*|0$";

  /**
   * 手机号码（精确）
   *
   * <p>移动：134(0-8)、135、136、137、138、139、147、150、151、152、157、158、159、178、182、183、184、187、188
   *
   * <p>联通：130、131、132、145、155、156、175、176、185、186
   *
   * <p>电信：133、153、173、177、180、181、189
   *
   * <p>全球星：1349
   *
   * <p>虚拟运营商：170
   */
  public static final String REGEX_MOBILEPHONE =
      "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|(147)" + ")" + "\\d{8}$";

  /** 手机号码（简单） */
  public static final String REGEX_MOBILEEPHONE_SIMPLE = "^[1]\\d{10}$";

  /** 电话号码 */
  public static final String REGEX_TELEPHONE = "^0\\d{2,3}[- ]?\\d{7,8}";

  /** QQ号码 */
  public static final String REGEX_TENCENT_QQ = "[1-9][0-9]{4,}";

  /** URL */
  public static final String REGEX_URL = "[a-zA-z]+://[^\\s]*";

  /** 用户名（取值范围为 a-z、A-Z、0-9、"_"、汉字，不能以"_"结尾，用户名必须是 1-20 位） */
  public static final String REGEX_USERNAME = "^[\\w\\u4e00-\\u9fa5]{1,20}(?<!_)$";

  /** 汉字 */
  public static final String REGEX_ZH_CN = "^[\\u4e00-\\u9fa5]+$";

  /** 邮政编码（中国） */
  public static final String REGEX_ZIP_CODE_CN = "[1-9]\\d{5}(?!\\d)";
}
