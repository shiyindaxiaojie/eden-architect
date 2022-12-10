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

package org.ylzl.eden.spring.data.redis.jedis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.Set;

/**
 * Jedis 集群
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class FixedJedisCluster extends JedisCluster {

	public FixedJedisCluster(
		Set<HostAndPort> jedisClusterNode,
		int connectionTimeout,
		int soTimeout,
		int maxAttempts,
		String password,
		GenericObjectPoolConfig poolConfig) {
		super(jedisClusterNode, connectionTimeout, soTimeout, maxAttempts, password, poolConfig);
		super.connectionHandler =
			new FixedJedisSlotBasedConnectionHandler(
				jedisClusterNode, poolConfig, connectionTimeout, soTimeout, password);
	}

	public FixedJedisSlotBasedConnectionHandler getConnectionHandler() {
		return (FixedJedisSlotBasedConnectionHandler) this.connectionHandler;
	}

	public void refreshCluster() { // 刷新集群信息，当集群信息发生变更时调用
		connectionHandler.renewSlotCache();
	}
}
