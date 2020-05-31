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

package org.ylzl.eden.spring.boot.data.hazelcast;

import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.spring.cache.HazelcastCacheManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.hazelcast.HazelcastAutoConfiguration;
import org.springframework.boot.autoconfigure.hazelcast.HazelcastProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.spring.boot.framework.core.FrameworkAutoConfiguration;
import org.ylzl.eden.spring.boot.framework.core.FrameworkConstants;
import org.ylzl.eden.spring.boot.framework.info.InfoContributorAutoConfiguration;
import org.ylzl.eden.spring.boot.framework.info.InfoContributorProvider;

/**
 * Hazelcast 自动配置
 *
 * @author gyl
 * @since 1.0.0
 */
@AutoConfigureAfter({FrameworkAutoConfiguration.class, InfoContributorAutoConfiguration.class})
@AutoConfigureBefore(HazelcastAutoConfiguration.class)
@ConditionalOnClass({HazelcastInstance.class})
@ConditionalOnExpression(EnhancedHazelcastAutoConfiguration.EXPS_HAZELCAST_ENABLED)
@ConditionalOnMissingBean({HazelcastInstance.class})
@EnableCaching
@EnableConfigurationProperties({HazelcastProperties.class, EnhancedHazelcastProperties.class})
@Slf4j
@Configuration
public class EnhancedHazelcastAutoConfiguration implements DisposableBean {

  public static final String EXPS_HAZELCAST_ENABLED =
      "${" + FrameworkConstants.PROP_PREFIX + ".hazelcast.enabled:true}";

  private static final String DEFAULT_MAP_CONFIGS_KEY = "default";

  private static final String DEFAULT_DOMAIN_PATTERN = "domain.*";

  private static final String MSG_AUTOWIRED_HAZELCAST = "Autowired Hazelcast";

  private static final String MSG_AUTOWIRED_HAZELCAST_MGR = "Autowired Hazelcast CacheManager";

  private static final String MSG_STOPING_HAZELCAST = "Shutdown Hazelcast";

  private static final String MSG_ALREADY_STARTED_HAZELCAST = "Hazelcast is already running";

  @Value(FrameworkConstants.NAME_PATTERN)
  private String applicationName;

  private final EnhancedHazelcastProperties enhancedHazelcastProperties;

  public EnhancedHazelcastAutoConfiguration(
      EnhancedHazelcastProperties enhancedHazelcastProperties) {
    this.enhancedHazelcastProperties = enhancedHazelcastProperties;
  }

  @Override
  public void destroy() {
    log.debug(MSG_STOPING_HAZELCAST);
    HazelcastInstance hazelcastInstance = Hazelcast.getHazelcastInstanceByName(applicationName);
    if (hazelcastInstance != null) {
      hazelcastInstance.shutdown();
    }
  }

  @ConditionalOnMissingBean
  @Bean
  public HazelcastInstance hazelcastInstance(InfoContributorProvider infoContributorProvider) {
    log.debug(MSG_AUTOWIRED_HAZELCAST);
    HazelcastInstance hazelCastInstance = Hazelcast.getHazelcastInstanceByName(applicationName);
    if (hazelCastInstance != null) {
      log.debug(MSG_ALREADY_STARTED_HAZELCAST);
      return hazelCastInstance;
    }
    Config config = this.initializeDefaultConfig(infoContributorProvider);
    return Hazelcast.newHazelcastInstance(config);
  }

  @ConditionalOnMissingBean
  @Bean
  public CacheManager hazelcastCacheManager(HazelcastInstance hazelcastInstance) {
    log.debug(MSG_AUTOWIRED_HAZELCAST_MGR);
    return new HazelcastCacheManager(hazelcastInstance);
  }

  protected Config initializeDefaultConfig(InfoContributorProvider infoContributorProvider) {
    Config config = new Config();
    config.setInstanceName(applicationName);
    config.setManagementCenterConfig(this.initializeDefaultManagementCenterConfig());
    config.getMapConfigs().put(DEFAULT_MAP_CONFIGS_KEY, this.initializeDefaultMapConfig());
    config
        .getMapConfigs()
        .put(
            infoContributorProvider.resolvePackage(DEFAULT_DOMAIN_PATTERN),
            this.initializeDomainMapConfig());
    this.addDefaultConfig(config);
    return config;
  }

  protected void addDefaultConfig(Config config) {
    config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
  }

  protected ManagementCenterConfig initializeDefaultManagementCenterConfig() {
    ManagementCenterConfig managementCenterConfig = new ManagementCenterConfig();
    managementCenterConfig.setEnabled(
        enhancedHazelcastProperties.getManagementCenter().isEnabled());
    managementCenterConfig.setUrl(enhancedHazelcastProperties.getManagementCenter().getUrl());
    managementCenterConfig.setUpdateInterval(
        enhancedHazelcastProperties.getManagementCenter().getUpdateInterval());
    return managementCenterConfig;
  }

  protected MapConfig initializeDefaultMapConfig() {
    MapConfig mapConfig = new MapConfig();
    mapConfig.setBackupCount(enhancedHazelcastProperties.getBackupCount());
    mapConfig.setEvictionPolicy(EvictionPolicy.LRU);
    mapConfig.setMaxSizeConfig(new MaxSizeConfig(0, MaxSizeConfig.MaxSizePolicy.USED_HEAP_SIZE));
    return mapConfig;
  }

  protected MapConfig initializeDomainMapConfig() {
    MapConfig mapConfig = new MapConfig();
    mapConfig.setTimeToLiveSeconds(enhancedHazelcastProperties.getTimeToLiveSeconds());
    return mapConfig;
  }
}
