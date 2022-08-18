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

package org.ylzl.eden.spring.data.redis.jedis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisSlotBasedConnectionHandler;
import redis.clients.jedis.exceptions.JedisNoReachableClusterNodeException;

import java.util.Set;

/**
 * Jedis 槽位连接处理器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class FixedJedisSlotBasedConnectionHandler extends JedisSlotBasedConnectionHandler {

	public FixedJedisSlotBasedConnectionHandler(
		Set<HostAndPort> nodes,
		GenericObjectPoolConfig poolConfig,
		int connectionTimeout,
		int soTimeout,
		String password) {
		super(nodes, poolConfig, connectionTimeout, soTimeout, password);
	}

	public JedisPool getJedisPoolFromSlot(int slot) {
		JedisPool connectionPool = cache.getSlotPool(slot);
		if (connectionPool != null) {
			return connectionPool;
		} else {
			renewSlotCache();
			connectionPool = cache.getSlotPool(slot);
			if (connectionPool != null) {
				return connectionPool;
			} else {
				throw new JedisNoReachableClusterNodeException(
					"No reachable node in cluster for slot " + slot);
			}
		}
	}
}
