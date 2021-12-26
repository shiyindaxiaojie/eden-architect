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

package org.ylzl.eden.spring.security.core.enums;

import org.ylzl.eden.spring.security.core.SecurityConstants;

import java.text.MessageFormat;

/**
 * 认证类型枚举
 *
 * @author gyl
 * @since 1.0.0
 */
public enum AuthenticationTypeEnum {
  BASIC_AUTH(SecurityConstants.BASIC_AUTH + "{0}"),
  BEARER_TOKEN(SecurityConstants.BEARER_TOKEN + "{0}"),
  DIGEST_AUTH(
      "Digest username=\"{0}\", realm=\"{1}\", nonce=\"{2}\", uri=\"/\", algorithm=\"{3}\", response=\"{4}\", opaque=\"{5}\""),
  HAWK_AUTHENTICATION("Hawk id=\"{0}\", ts=\"{1}\", nonce=\"{2}\", ext=\"{3}\", mac=\"{4}\""),
  AWS_SIGNATURE(
      "AWS4-HMAC-SHA256 Credential={0}/{1}/{2}/{3}/aws4_request, SignedHeaders=content-type;host;x-amz-date;x-amz-security-token, "
          + "Signature={5}"),
  NTLM_AUTHENTICATION(""),
  OAUTH1(
      "OAuth realm=\"{0}\",oauth_consumer_key=\"{1}\",oauth_token=\"{2}\",oauth_signature_method=\"{3}\",oauth_timestamp=\"{4}\","
          + "oauth_nonce=\"{5}\",oauth_version=\"1.0\",oauth_signature=\"{6}\""),
  OATUH2(SecurityConstants.BEARER_TOKEN + "{0}");

  private String pattern;

  AuthenticationTypeEnum(String pattern) {
    this.pattern = pattern;
  }

  public String getAuthorization(String... value) {
    return MessageFormat.format(pattern, value);
  }
}
