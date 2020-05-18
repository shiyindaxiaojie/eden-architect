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

package org.ylzl.eden.spring.boot.cloud.hazelcast;

import com.hazelcast.config.Config;
import com.hazelcast.core.HazelcastInstance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.hazelcast.HazelcastAutoConfiguration;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.ylzl.eden.spring.boot.cloud.core.CloudConstants;
import org.ylzl.eden.spring.boot.integration.hazelcast.EnhancedHazelcastAutoConfiguration;
import org.ylzl.eden.spring.boot.integration.hazelcast.EnhancedHazelcastProperties;

/**
 * 基于 Spring Cloud 的 Hazelcast 自动配置
 *
 * @author gyl
 * @since 1.0.0
 */
@AutoConfigureBefore({HazelcastAutoConfiguration.class, EnhancedHazelcastAutoConfiguration.class})
@ConditionalOnClass({HazelcastInstance.class})
@ConditionalOnExpression(DiscoveryHazelcastAutoConfiguration.EXPS_CLOUD_HAZELCAST_ENABLED)
@ConditionalOnBean({DiscoveryClient.class})
@ConditionalOnMissingBean({HazelcastInstance.class})
@EnableCaching
@EnableConfigurationProperties({EnhancedHazelcastProperties.class})
@Slf4j
@Configuration
public class DiscoveryHazelcastAutoConfiguration extends EnhancedHazelcastAutoConfiguration {

  public static final String EXPS_CLOUD_HAZELCAST_ENABLED =
      "${" + CloudConstants.PROP_PREFIX + ".hazelcast.enabled:true}";

  private static final String MSG_DICOVERY_HAZELCAST_IN_DEV =
      "Configuring Hazelcast clustering in development";

  private static final String MSG_DICOVERY_HAZELCAST =
      "Configuring Hazelcast clustering for instanceId: {}, members: {}";

  private static final String PROP_HAZELCAST_LOCAL_ADDRESS = "hazelcast.local.localAddress";

  private static final String LOCAL_ADDRESS = "127.0.0.1";

  private final Environment environment;

  private final ServerProperties serverProperties;

  private final DiscoveryClient discoveryClient;

  public DiscoveryHazelcastAutoConfiguration(
      EnhancedHazelcastProperties enhancedHazelcastProperties,
      Environment environment,
      ServerProperties serverProperties,
      DiscoveryClient discoveryClient) {
    super(enhancedHazelcastProperties);
    this.environment = environment;
    this.serverProperties = serverProperties;
    this.discoveryClient = discoveryClient;
  }

  @Override
  protected void addDefaultConfig(Config config) {
    super.addDefaultConfig(config);
    /*if (this.registration != null) {
        discoveryHazelcast(config);
    }*/
  }

  /*@SuppressWarnings("unchecked")
  private void discoveryHazelcast(Config config) {
      String serviceId = registration.getId();
      NetworkConfig networkConfig = config.getNetworkConfig();
      if (environment.acceptsProfiles(Profile.of(ProfileConstants.SPRING_PROFILE_DEVELOPMENT))) {
          log.debug(MSG_DICOVERY_HAZELCAST_IN_DEV);
          System.setProperty(PROP_HAZELCAST_LOCAL_ADDRESS, LOCAL_ADDRESS);
          networkConfig.setPort(serverProperties.getPort() + NetworkConfig.DEFAULT_PORT);

          for (ServiceInstance instance : discoveryClient.getInstances(serviceId)) {
              String clusterMember = StringUtils.join(LOCAL_ADDRESS, StringConstants.COLON, (instance.getPort() + NetworkConfig.DEFAULT_PORT));
              networkConfig.getJoin().getTcpIpConfig().addMember(clusterMember);
          }
      } else {
          networkConfig.setPort(NetworkConfig.DEFAULT_PORT);

          for (ServiceInstance instance : discoveryClient.getInstances(serviceId)) {
              String clusterMember = StringUtils.join(instance.getHost(), StringConstants.COLON, NetworkConfig.DEFAULT_PORT);
              networkConfig.getJoin().getTcpIpConfig().addMember(clusterMember);
          }
      }
      log.debug(MSG_DICOVERY_HAZELCAST, serviceId, networkConfig.getJoin().getTcpIpConfig().getMembers());
  }*/
}
