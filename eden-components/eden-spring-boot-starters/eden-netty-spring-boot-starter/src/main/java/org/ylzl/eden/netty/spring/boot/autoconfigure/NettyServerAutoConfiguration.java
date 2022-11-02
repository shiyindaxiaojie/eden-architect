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

package org.ylzl.eden.netty.spring.boot.autoconfigure;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.commons.collections.CollectionUtils;
import org.ylzl.eden.netty.spring.boot.env.NettyServerProperties;
import org.ylzl.eden.spring.integration.netty.bootstrap.NettyServer;

import java.util.List;

/**
 * Netty 服务端自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ConditionalOnClass(ServerBootstrap.class)
@ConditionalOnProperty(value = NettyServerProperties.PREFIX + ".enabled", matchIfMissing = true)
@EnableConfigurationProperties(NettyServerProperties.class)
@Slf4j
@Configuration(proxyBeanMethods = false)
public class NettyServerAutoConfiguration {

	private final NettyServerProperties properties;

	public NettyServerAutoConfiguration(NettyServerProperties properties) {
		this.properties = properties;
	}

	@ConditionalOnMissingBean
	@Bean
	public NettyServer nettyServer(
		@Autowired(required = false) List<ChannelHandler> channelHandlers,
		@Autowired(required = false) List<ChannelFutureListener> channelFutureListeners) {
		NettyServer nettyServer =
			new NettyServer(properties.getName(), properties.getHost(), properties.getPort());
		if (properties.getBossThreads() > 0) {
			nettyServer.setBossThreads(properties.getBossThreads());
		}
		if (properties.getWorkerThreads() > 0) {
			nettyServer.setWorkerThreads(properties.getWorkerThreads());
		}
		if (CollectionUtils.isNotEmpty(channelHandlers)) {
			nettyServer.addAllChannelHandlers(channelHandlers);
		}
		if (CollectionUtils.isNotEmpty(channelFutureListeners)) {
			nettyServer.addAllChannelFutureListeners(channelFutureListeners);
		}
		if (properties.isAutoStartup()) {
			nettyServer.setAutoStartup(properties.isAutoStartup());
		}
		return nettyServer;
	}
}
