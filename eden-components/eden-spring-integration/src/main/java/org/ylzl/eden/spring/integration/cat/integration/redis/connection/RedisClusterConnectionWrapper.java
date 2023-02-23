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

package org.ylzl.eden.spring.integration.cat.integration.redis.connection;

import org.springframework.data.redis.connection.ClusterInfo;
import org.springframework.data.redis.connection.RedisClusterConnection;
import org.springframework.data.redis.connection.RedisClusterNode;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.types.RedisClientInfo;
import org.ylzl.eden.spring.integration.cat.integration.redis.RedisCatSupport;
import org.ylzl.eden.spring.integration.cat.integration.redis.command.RedisCommand;

import java.util.*;

/**
 * Redis 集群连接包装
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class RedisClusterConnectionWrapper extends RedisConnectionWrapper implements RedisClusterConnection {

	private final RedisClusterConnection connection;

	public RedisClusterConnectionWrapper(RedisClusterConnection connection) {
		super(connection);
		this.connection = connection;
	}

	@Override
	public String ping(RedisClusterNode node) {
		return RedisCatSupport.execute(RedisCommand.PING, () -> connection.ping(node));
	}

	@Override
	public Set<byte[]> keys(RedisClusterNode node, byte[] pattern) {
		return RedisCatSupport.execute(RedisCommand.KEYS, () -> connection.keys(node, pattern));
	}

	@Override
	public Cursor<byte[]> scan(RedisClusterNode node, ScanOptions options) {
		return RedisCatSupport.execute(RedisCommand.SCAN, () -> connection.scan(node, options));
	}

	@Override
	public byte[] randomKey(RedisClusterNode node) {
		return RedisCatSupport.execute(RedisCommand.RANDOMKEY, () -> connection.randomKey(node));
	}

	@Override
	public <T> T execute(String command, byte[] key, Collection<byte[]> args) {
		return RedisCatSupport.execute(RedisCommand.EXECUTE, key, () -> connection.execute(command, key, args));
	}

	@Override
	public Iterable<RedisClusterNode> clusterGetNodes() {
		return RedisCatSupport.execute(RedisCommand.CLUSTER_NODES, () -> connection.clusterGetNodes());
	}

	@Override
	public Collection<RedisClusterNode> clusterGetSlaves(RedisClusterNode node) {
		return RedisCatSupport.execute(RedisCommand.CLUSTER_SLAVES, () -> connection.clusterGetSlaves(node));
	}

	@Override
	public Map<RedisClusterNode, Collection<RedisClusterNode>> clusterGetMasterSlaveMap() {
		return RedisCatSupport.execute(RedisCommand.CLUSTER_MASTER_SLAVE_MAP, () -> connection.clusterGetMasterSlaveMap());
	}

	@Override
	public Integer clusterGetSlotForKey(byte[] key) {
		return RedisCatSupport.execute(RedisCommand.CLUSTER_KEYSLOT, key, () -> connection.clusterGetSlotForKey(key));
	}

	@Override
	public RedisClusterNode clusterGetNodeForSlot(int slot) {
		return RedisCatSupport.execute(RedisCommand.CLUSTER_NODE_FOR_SLOT, () -> connection.clusterGetNodeForSlot(slot));
	}

	@Override
	public RedisClusterNode clusterGetNodeForKey(byte[] key) {
		return RedisCatSupport.execute(RedisCommand.CLUSTER_NODE_FOR_KEY, key, () -> connection.clusterGetNodeForKey(key));
	}

	@Override
	public ClusterInfo clusterGetClusterInfo() {
		return RedisCatSupport.execute(RedisCommand.CLUSTER_INFO, () -> connection.clusterGetClusterInfo());
	}

	@Override
	public void clusterAddSlots(RedisClusterNode node, int... slots) {
		RedisCatSupport.execute(RedisCommand.CLUSTER_ADDSLOTS, () -> connection.clusterAddSlots(node, slots));
	}

	@Override
	public void clusterAddSlots(RedisClusterNode node, RedisClusterNode.SlotRange range) {
		RedisCatSupport.execute(RedisCommand.CLUSTER_ADDSLOTS, () -> connection.clusterAddSlots(node, range));
	}

	@Override
	public Long clusterCountKeysInSlot(int slot) {
		return RedisCatSupport.execute(RedisCommand.CLUSTER_COUNTKEYSINSLOT, () -> connection.clusterCountKeysInSlot(slot));
	}

	@Override
	public void clusterDeleteSlots(RedisClusterNode node, int... slots) {
		RedisCatSupport.execute(RedisCommand.CLUSTER_DELSLOTS, () -> connection.clusterDeleteSlots(node, slots));
	}

	@Override
	public void clusterDeleteSlotsInRange(RedisClusterNode node, RedisClusterNode.SlotRange range) {
		RedisCatSupport.execute(RedisCommand.CLUSTER_DELSLOTS, () -> connection.clusterDeleteSlotsInRange(node, range));
	}

	@Override
	public void clusterForget(RedisClusterNode node) {
		RedisCatSupport.execute(RedisCommand.CLUSTER_FORGET, () -> connection.clusterForget(node));
	}

	@Override
	public void clusterMeet(RedisClusterNode node) {
		RedisCatSupport.execute(RedisCommand.CLUSTER_MEET, () -> connection.clusterMeet(node));
	}

	@Override
	public void clusterSetSlot(RedisClusterNode node, int slot, AddSlots mode) {
		RedisCatSupport.execute(RedisCommand.CLUSTER_SETSLOT, () -> connection.clusterSetSlot(node, slot, mode));
	}

	@Override
	public List<byte[]> clusterGetKeysInSlot(int slot, Integer count) {
		return RedisCatSupport.execute(RedisCommand.CLUSTER_GETKEYSINSLOT, () -> connection.clusterGetKeysInSlot(slot, count));
	}

	@Override
	public void clusterReplicate(RedisClusterNode master, RedisClusterNode slave) {
		RedisCatSupport.execute(RedisCommand.CLUSTER_REPLICATE, () -> connection.clusterReplicate(master, slave));
	}

	@Override
	public void bgReWriteAof(RedisClusterNode node) {
		RedisCatSupport.execute(RedisCommand.BGREWRITEAOF, () -> connection.bgReWriteAof(node));
	}

	@Override
	public void bgSave(RedisClusterNode node) {
		RedisCatSupport.execute(RedisCommand.BGSAVE, () -> connection.bgSave(node));
	}

	@Override
	public Long lastSave(RedisClusterNode node) {
		return RedisCatSupport.execute(RedisCommand.LASTSAVE, () -> connection.lastSave(node));
	}

	@Override
	public void save(RedisClusterNode node) {
		RedisCatSupport.execute(RedisCommand.SAVE, () -> connection.save(node));
	}

	@Override
	public Long dbSize(RedisClusterNode node) {
		return RedisCatSupport.execute(RedisCommand.DBSIZE, () -> connection.dbSize(node));
	}

	@Override
	public void flushDb(RedisClusterNode node) {
		RedisCatSupport.execute(RedisCommand.FLUSHDB, () -> connection.flushDb(node));
	}

	@Override
	public void flushAll(RedisClusterNode node) {
		RedisCatSupport.execute(RedisCommand.FLUSHALL, () -> connection.flushAll(node));
	}

	@Override
	public Properties info(RedisClusterNode node) {
		return RedisCatSupport.execute(RedisCommand.INFO, () -> connection.info(node));
	}

	@Override
	public Properties info(RedisClusterNode node, String section) {
		return RedisCatSupport.execute(RedisCommand.INFO, () -> connection.info(node, section));
	}

	@Override
	public void shutdown(RedisClusterNode node) {
		RedisCatSupport.execute(RedisCommand.SHUTDOWN, () -> connection.shutdown(node));
	}

	@Override
	public Properties getConfig(RedisClusterNode node, String pattern) {
		return RedisCatSupport.execute(RedisCommand.CONFIG_GET, () -> connection.getConfig(node, pattern));
	}

	@Override
	public void setConfig(RedisClusterNode node, String param, String value) {
		RedisCatSupport.execute(RedisCommand.CONFIG_GET, () -> connection.setConfig(node, param, value));
	}

	@Override
	public void resetConfigStats(RedisClusterNode node) {
		RedisCatSupport.execute(RedisCommand.CONFIG_RESETSTAT, () -> connection.resetConfigStats(node));
	}

	@Override
	public Long time(RedisClusterNode node) {
		return RedisCatSupport.execute(RedisCommand.TIME, () -> connection.time(node));
	}

	@Override
	public List<RedisClientInfo> getClientList(RedisClusterNode node) {
		return RedisCatSupport.execute(RedisCommand.CLIENT_LIST, () -> connection.getClientList(node));
	}
}
