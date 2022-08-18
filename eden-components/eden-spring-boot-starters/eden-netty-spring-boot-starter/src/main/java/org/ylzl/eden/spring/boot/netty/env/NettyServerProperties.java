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

package org.ylzl.eden.spring.boot.netty.env;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.ylzl.eden.spring.integration.netty.channel.ChannelOptions;

/**
 * Netty 服务端配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Getter
@Setter
@ConfigurationProperties(prefix = NettyServerProperties.PREFIX)
public final class NettyServerProperties {

	public static final String PREFIX = "netty.server";

	private boolean enabled = false;

	private boolean autoStartup = false;

	private String name;

	private String host = "127.0.0.1";

	private int port = 18080;

	private int bossThreads = Runtime.getRuntime().availableProcessors();

	private int workerThreads = Runtime.getRuntime().availableProcessors();

	@NestedConfigurationProperty
	private ChannelOptions channelOptions;

	@NestedConfigurationProperty
	private ChannelOptions childChannelOptions;

}
