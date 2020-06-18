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

package org.ylzl.eden.spring.boot.framework.undertow;

import io.undertow.UndertowOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.context.annotation.Configuration;

/**
 * Undertow SSL 自动配置类
 *
 * @author gyl
 * @since 2.0.0
 */
@ConditionalOnProperty({"server.ssl.ciphers", "server.ssl.key-store"})
@ConditionalOnClass({UndertowServletWebServerFactory.class, UndertowOptions.class})
@Slf4j
@Configuration
public class UndertowSSLAutoConfiguration {

  private static final String MSG_CONFIG_UNDERTOW_SSL =
      "Configuring Undertow Setting user cipher suite order to true";

  private final UndertowServletWebServerFactory factory;

  public UndertowSSLAutoConfiguration(
      UndertowServletWebServerFactory undertowServletWebServerFactory) {
    this.factory = undertowServletWebServerFactory;
    configuringUserCipherSuiteOrder();
  }

  private void configuringUserCipherSuiteOrder() {
    log.info(MSG_CONFIG_UNDERTOW_SSL);
    factory.addBuilderCustomizers(
        builder ->
            builder.setSocketOption(UndertowOptions.SSL_USER_CIPHER_SUITES_ORDER, Boolean.TRUE));
  }
}
