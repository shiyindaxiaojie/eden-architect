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

package org.ylzl.eden.spring.boot.cloud.profile.endpoint;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.endpoint.AbstractEndpoint;
import org.springframework.core.env.Environment;
import org.ylzl.eden.spring.boot.cloud.configserver.ConfigServerProperties;
import org.ylzl.eden.spring.boot.cloud.profile.ProfileProperties;
import org.ylzl.eden.spring.boot.framework.core.util.SpringProfileUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 运行环境信息端点
 *
 * @author gyl
 * @since 0.0.1
 */
@Slf4j
public class ProfileEndpoint extends AbstractEndpoint<Profile> {

  public static final String ENDPOINT_ID = "profiles";

  private final Environment env;

  private final ProfileProperties profileProperties;

  private final ConfigServerProperties configServerProperties;

  public ProfileEndpoint(
      Environment env,
      ProfileProperties profileProperties,
      ConfigServerProperties configServerProperties) {
    super(ENDPOINT_ID);
    this.env = env;
    this.profileProperties = profileProperties;
    this.configServerProperties = configServerProperties;
  }

  @Override
  public Profile invoke() {
    String[] activeProfiles = SpringProfileUtils.getActiveProfiles(env);
    return new Profile(
        activeProfiles, getRibbonEnv(activeProfiles), configServerProperties.getComposite());
  }

  private String getRibbonEnv(String[] activeProfiles) {
    if (activeProfiles != null) {
      List<String> ribbonProfiles =
          new ArrayList<>(Arrays.asList(profileProperties.getDisplayOnActiveProfiles()));
      List<String> springBootProfiles = Arrays.asList(activeProfiles);
      ribbonProfiles.retainAll(springBootProfiles);
      if (!ribbonProfiles.isEmpty()) {
        return ribbonProfiles.get(0);
      }
    }
    return null;
  }
}
