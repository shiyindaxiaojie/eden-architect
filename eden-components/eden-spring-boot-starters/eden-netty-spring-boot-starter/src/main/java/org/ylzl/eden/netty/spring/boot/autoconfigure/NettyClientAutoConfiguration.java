/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ylzl.eden.netty.spring.boot.autoconfigure;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.ylzl.eden.commons.collections.CollectionUtils;
import org.ylzl.eden.netty.spring.boot.env.NettyClientProperties;
import org.ylzl.eden.spring.integration.netty.NettyClient;

import java.util.List;

/**
 * Netty 客户端自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ConditionalOnClass(Bootstrap.class)
@ConditionalOnProperty(value = NettyClientProperties.PREFIX + ".enabled", matchIfMissing = true)
@EnableConfigurationProperties(NettyClientProperties.class)
@Slf4j
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Configuration(proxyBeanMethods = false)
public class NettyClientAutoConfiguration {

	private final NettyClientProperties properties;

	public NettyClientAutoConfiguration(NettyClientProperties properties) {
		this.properties = properties;
	}

	@ConditionalOnMissingBean
	@Bean
	public NettyClient nettyClient(
		@Autowired(required = false) List<ChannelHandler> channelHandlers,
		@Autowired(required = false) List<ChannelFutureListener> channelFutureListeners) {
		NettyClient nettyClient =
			new NettyClient(properties.getName(), properties.getHost(), properties.getPort());
		if (properties.getChannelThreads() > 0) {
			nettyClient.setChannelThreads(properties.getChannelThreads());
		}
		if (CollectionUtils.isNotEmpty(channelHandlers)) {
			nettyClient.addAllChannelHandlers(channelHandlers);
		}
		if (CollectionUtils.isNotEmpty(channelFutureListeners)) {
			nettyClient.addAllChannelFutureListeners(channelFutureListeners);
		}
		if (properties.isAutoStartup()) {
			nettyClient.setAutoStartup(properties.isAutoStartup());
		}
		return nettyClient;
	}
}
