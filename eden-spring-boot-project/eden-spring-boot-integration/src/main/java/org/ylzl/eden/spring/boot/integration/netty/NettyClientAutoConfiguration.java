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

package org.ylzl.eden.spring.boot.integration.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.spring.boot.commons.collections.CollectionUtils;
import org.ylzl.eden.spring.boot.integration.core.IntegrationConstants;
import org.ylzl.eden.spring.boot.integration.netty.bootstrap.NettyClient;

import java.util.List;

/**
 * Netty 客户端自动配置
 *
 * @author gyl
 * @since 1.0.0
 */
@ConditionalOnClass(Bootstrap.class)
@ConditionalOnExpression(NettyClientAutoConfiguration.EXP_NETTY_CLIENT_ENABLED)
@EnableConfigurationProperties(NettyProperties.class)
@Slf4j
@Configuration
public class NettyClientAutoConfiguration {

  public static final String EXP_NETTY_CLIENT_ENABLED =
      "${" + IntegrationConstants.PROP_PREFIX + ".netty.client.enabled:false}";

  private final NettyProperties.Client properties;

  public NettyClientAutoConfiguration(NettyProperties properties) {
    this.properties = properties.getClient();
  }

  @ConditionalOnMissingBean
  @Bean
  public NettyClient nettyClient(
      @Autowired(required = false) List<ChannelHandler> channelHandlers,
      @Autowired(required = false) List<ChannelFutureListener> channelFutureListeners) {
    NettyClient nettyClient =
        new NettyClient(properties.getName(), properties.getHost(), properties.getPort());
    if (properties.getChannelThreads() != null) {
      nettyClient.setChannelThreads(properties.getChannelThreads());
    }
    if (CollectionUtils.isNotEmpty(channelHandlers)) {
      nettyClient.addAllChannelHandlers(channelHandlers);
    }
    if (CollectionUtils.isNotEmpty(channelFutureListeners)) {
      nettyClient.addAllChannelFutureListeners(channelFutureListeners);
    }
    if (properties.getAutoStartup()) {
      nettyClient.setAutoStartup(properties.getAutoStartup());
    }
    return nettyClient;
  }
}
