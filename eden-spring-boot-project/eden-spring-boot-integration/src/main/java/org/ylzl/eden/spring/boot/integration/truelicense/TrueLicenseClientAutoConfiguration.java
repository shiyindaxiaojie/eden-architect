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

package org.ylzl.eden.spring.boot.integration.truelicense;

import de.schlichtherle.license.LicenseManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.ylzl.eden.spring.boot.integration.core.IntegrationConstants;
import org.ylzl.eden.spring.boot.integration.truelicense.manager.LicenseInstall;
import org.ylzl.eden.spring.boot.integration.truelicense.manager.LicenseVerify;

/**
 * TrueLicense 客户端配置
 *
 * @author gyl
 * @since 1.0.0
 */
@ConditionalOnExpression(TrueLicenseClientAutoConfiguration.EXP_TRUE_LICENSE_ENABLED)
@Import(TrueLicenseConfiguration.class)
@Slf4j
@Configuration
public class TrueLicenseClientAutoConfiguration {

  public static final String EXP_TRUE_LICENSE_ENABLED =
      "${" + IntegrationConstants.PROP_PREFIX + ".true-license.client.enabled:false}";

  private static final String MSG_AUTOWIRED_TRUE_LICENSE_INSTALL =
      "Autowired TureLicense install service";

  private static final String MSG_AUTOWIRED_TRUE_LICENSE_VERIFY =
      "Autowired TureLicense verify service";

  private final LicenseManager licenseManager;

  public TrueLicenseClientAutoConfiguration(LicenseManager licenseManager) {
    this.licenseManager = licenseManager;
  }

  @ConditionalOnMissingBean
  @Bean
  public LicenseInstall licenseInstall(TrueLicenseProperties trueLicenseProperties) {
    log.debug(MSG_AUTOWIRED_TRUE_LICENSE_INSTALL);
    return new LicenseInstall(trueLicenseProperties, licenseManager);
  }

  @ConditionalOnMissingBean
  @Bean
  public LicenseVerify licenseVerify() {
    log.debug(MSG_AUTOWIRED_TRUE_LICENSE_VERIFY);
    return new LicenseVerify(licenseManager);
  }
}
