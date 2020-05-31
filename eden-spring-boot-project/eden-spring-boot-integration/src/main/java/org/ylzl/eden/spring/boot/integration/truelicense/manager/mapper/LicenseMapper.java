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
package org.ylzl.eden.spring.boot.integration.truelicense.manager.mapper;

import de.schlichtherle.license.LicenseContent;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;
import org.ylzl.eden.spring.boot.integration.truelicense.TrueLicenseProperties;
import org.ylzl.eden.spring.boot.integration.truelicense.manager.EnhancedLicenseContent;

/**
 * 许可证映射器
 *
 * @author gyl
 * @since 1.0.0
 */
@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface LicenseMapper {

  LicenseMapper INSTANCE = Mappers.getMapper(LicenseMapper.class);

  void updateLicenseStoreFromTrueLicenseProperties(
      TrueLicenseProperties trueLicenseProperties,
      @MappingTarget EnhancedLicenseContent enhancedLicenseContent);

  void updateLicenseContentFromLicenseStore(
      EnhancedLicenseContent enhancedLicenseContent, @MappingTarget LicenseContent licenseContent);
}
