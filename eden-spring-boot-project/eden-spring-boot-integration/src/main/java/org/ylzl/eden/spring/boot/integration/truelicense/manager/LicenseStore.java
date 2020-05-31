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

package org.ylzl.eden.spring.boot.integration.truelicense.manager;

import de.schlichtherle.license.LicenseContent;
import de.schlichtherle.license.LicenseManager;
import lombok.extern.slf4j.Slf4j;
import org.ylzl.eden.spring.boot.commons.lang.ObjectUtils;
import org.ylzl.eden.spring.boot.commons.lang.StringUtils;
import org.ylzl.eden.spring.boot.commons.lang.time.DateUtils;
import org.ylzl.eden.spring.boot.integration.truelicense.manager.mapper.LicenseMapper;

import javax.security.auth.x500.X500Principal;
import java.io.File;
import java.util.Calendar;
import java.util.Date;

/**
 * 许可证服务
 *
 * @author gyl
 * @since 1.0.0
 */
@Slf4j
public class LicenseStore {

  private static final X500Principal DEFAULT_HOLDER_AND_ISSUER =
      new X500Principal("CN=localhost, OU=localhost, O=localhost, L=SH, ST=SH, C=CN");

  private static final String DEFAULT_CONSUMER_TYPE = "user";

  private final LicenseManager licenseManager;

  public LicenseStore(LicenseManager licenseManager) {
    this.licenseManager = licenseManager;
  }

  public boolean store(EnhancedLicenseContent enhancedLicenseContent) {
    // 如果没有设置类型，按用户类型设置
    if (StringUtils.isNull(enhancedLicenseContent.getConsumerType())) {
      enhancedLicenseContent.setConsumerType(DEFAULT_CONSUMER_TYPE);
    }

    // 签发时间默认为当前系统时间
    enhancedLicenseContent.setIssued(new Date());

    // 如果没有设置生效时间，按签发时间生效
    if (ObjectUtils.isNull(enhancedLicenseContent.getNotBefore())) {
      enhancedLicenseContent.setNotBefore(enhancedLicenseContent.getIssued());
    }

    // 如果没有设置到期时间，默认为 1 个月有效期
    if (ObjectUtils.isNull(enhancedLicenseContent.getNotAfter())) {
      enhancedLicenseContent.setNotAfter(
          DateUtils.dateAdd(enhancedLicenseContent.getNotBefore(), 1, Calendar.MONTH));
    }

    LicenseContent licenseContent = new LicenseContent();
    licenseContent.setHolder(DEFAULT_HOLDER_AND_ISSUER);
    licenseContent.setIssuer(DEFAULT_HOLDER_AND_ISSUER);
    LicenseMapper.INSTANCE.updateLicenseContentFromLicenseStore(
        enhancedLicenseContent, licenseContent);
    File file = new File(enhancedLicenseContent.getLicensePath());

    try {
      licenseManager.store(licenseContent, file);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return false;
    }
    return true;
  }
}
