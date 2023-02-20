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

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.data.redis.core.types.RedisClientInfo;
import org.ylzl.eden.spring.integration.cat.integration.redis.CatRedisSupport;
import org.ylzl.eden.spring.integration.cat.integration.redis.command.RedisCommand;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * TODO
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@RequiredArgsConstructor
public class CatRedisConnection implements RedisConnection {

	private final RedisConnection connection;

	@Override
	public void close() throws DataAccessException {
		connection.close();
	}

	@Override
	public boolean isClosed() {
		return connection.isClosed();
	}

	@Override
	public Object getNativeConnection() {
		return connection.getNativeConnection();
	}

	@Override
	public boolean isQueueing() {
		return connection.isQueueing();
	}

	@Override
	public boolean isPipelined() {
		return connection.isPipelined();
	}

	@Override
	public void openPipeline() {
		connection.openPipeline();
	}

	@Override
	public List<Object> closePipeline() throws RedisPipelineException {
		return connection.closePipeline();
	}

	@Override
	public RedisSentinelConnection getSentinelConnection() {
		return connection.getSentinelConnection();
	}

	@Override
	public Object execute(String command, byte[]... args) {
		return CatRedisSupport.execute(RedisCommand.EXECUTE, () -> connection.execute(command, args));
	}

	@Override
	public void select(int dbIndex) {
		CatRedisSupport.execute(RedisCommand.SELECT, () -> connection.select(dbIndex));
	}

	@Override
	public byte[] echo(byte[] message) {
		return CatRedisSupport.execute(RedisCommand.ECHO, () -> connection.echo(message));
	}

	@Override
	public String ping() {
		return CatRedisSupport.execute(RedisCommand.PING, () -> connection.ping());
	}

	@Override
	public Long geoAdd(byte[] key, Point point, byte[] member) {
		return CatRedisSupport.execute(RedisCommand.GEOADD, key, () -> connection.geoAdd(key, point, member));
	}

	@Override
	public Long geoAdd(byte[] key, Map<byte[], Point> memberCoordinateMap) {
		return CatRedisSupport.execute(RedisCommand.GEOADD, key, () -> connection.geoAdd(key, memberCoordinateMap));
	}

	@Override
	public Long geoAdd(byte[] key, Iterable<GeoLocation<byte[]>> locations) {
		return CatRedisSupport.execute(RedisCommand.GEOADD, key, () -> connection.geoAdd(key, locations));
	}

	@Override
	public Distance geoDist(byte[] key, byte[] member1, byte[] member2) {
		return CatRedisSupport.execute(RedisCommand.GEODIST, key, () -> connection.geoDist(key, member1, member2));
	}

	@Override
	public Distance geoDist(byte[] key, byte[] member1, byte[] member2, Metric metric) {
		return CatRedisSupport.execute(RedisCommand.GEODIST, key, () -> connection.geoDist(key, member1, member2, metric));
	}

	@Override
	public List<String> geoHash(byte[] key, byte[]... members) {
		return CatRedisSupport.execute(RedisCommand.GEOHASH, key, () -> connection.geoHash(key, members));
	}

	@Override
	public List<Point> geoPos(byte[] key, byte[]... members) {
		return CatRedisSupport.execute(RedisCommand.GEOPOS, key, () -> connection.geoPos(key, members));
	}

	@Override
	public GeoResults<GeoLocation<byte[]>> geoRadius(byte[] key, Circle within) {
		return CatRedisSupport.execute(RedisCommand.GEORADIUS, key, () -> connection.geoRadius(key, within));
	}

	@Override
	public GeoResults<GeoLocation<byte[]>> geoRadius(byte[] key, Circle within, GeoRadiusCommandArgs args) {
		return CatRedisSupport.execute(RedisCommand.GEORADIUS, key, () -> connection.geoRadius(key, within, args));
	}

	@Override
	public GeoResults<GeoLocation<byte[]>> geoRadiusByMember(byte[] key, byte[] member,
															 Distance radius) {
		return CatRedisSupport.execute(RedisCommand.GEORADIUSBYMEMBER, key, () -> connection.geoRadiusByMember(key, member, radius));
	}

	@Override
	public GeoResults<GeoLocation<byte[]>> geoRadiusByMember(byte[] key, byte[] member,
															 Distance radius, GeoRadiusCommandArgs args) {
		return CatRedisSupport.execute(RedisCommand.GEORADIUSBYMEMBER, key, () -> connection.geoRadiusByMember(key, member, radius, args));
	}

	@Override
	public Long geoRemove(byte[] key, byte[]... members) {
		return CatRedisSupport.execute(RedisCommand.GEOREMOVE, key, () -> connection.geoRemove(key, members));
	}

	@Override
	public Boolean hSet(byte[] key, byte[] field, byte[] value) {
		return CatRedisSupport.execute(RedisCommand.HSET, key, () -> connection.hSet(key, field, value));
	}

	@Override
	public Boolean hSetNX(byte[] key, byte[] field, byte[] value) {
		return CatRedisSupport.execute(RedisCommand.HSETNX, key, () -> connection.hSetNX(key, field, value));
	}

	@Override
	public byte[] hGet(byte[] key, byte[] field) {
		return CatRedisSupport.execute(RedisCommand.HGET, key, () -> connection.hGet(key, field));
	}

	@Override
	public List<byte[]> hMGet(byte[] key, byte[]... fields) {
		return CatRedisSupport.execute(RedisCommand.HMGET, key, () -> connection.hMGet(key, fields));
	}

	@Override
	public void hMSet(byte[] key, Map<byte[], byte[]> hashes) {
		CatRedisSupport.execute(RedisCommand.HMSET, key, () -> connection.hMSet(key, hashes));
	}

	@Override
	public Long hIncrBy(byte[] key, byte[] field, long delta) {
		return CatRedisSupport.execute(RedisCommand.HINCRBY, key, () -> connection.hIncrBy(key, field, delta));
	}

	@Override
	public Double hIncrBy(byte[] key, byte[] field, double delta) {
		return CatRedisSupport.execute(RedisCommand.HINCRBY, key, () -> connection.hIncrBy(key, field, delta));
	}

	@Override
	public Boolean hExists(byte[] key, byte[] field) {
		return CatRedisSupport.execute(RedisCommand.HEXISTS, key, () -> connection.hExists(key, field));
	}

	@Override
	public Long hDel(byte[] key, byte[]... fields) {
		return CatRedisSupport.execute(RedisCommand.HDEL, key, () -> connection.hDel(key));
	}

	@Override
	public Long hLen(byte[] key) {
		return CatRedisSupport.execute(RedisCommand.HLEN, key, () -> connection.hLen(key));
	}

	@Override
	public Set<byte[]> hKeys(byte[] key) {
		return CatRedisSupport.execute(RedisCommand.HKEYS, key, () -> connection.hKeys(key));
	}

	@Override
	public List<byte[]> hVals(byte[] key) {
		return CatRedisSupport.execute(RedisCommand.HVALS, key, () -> connection.hVals(key));
	}

	@Override
	public Map<byte[], byte[]> hGetAll(byte[] key) {
		return CatRedisSupport.execute(RedisCommand.HGETALL, key, () -> connection.hGetAll(key));
	}

	@Override
	public Cursor<Map.Entry<byte[], byte[]>> hScan(byte[] key, ScanOptions options) {
		return CatRedisSupport.execute(RedisCommand.HSCAN, key, () -> connection.hScan(key, options));
	}

	@Override
	public Long hStrLen(byte[] key, byte[] field) {
		return CatRedisSupport.execute(RedisCommand.HSTRLEN, key, () -> connection.hStrLen(key, field));
	}

	@Override
	public Long pfAdd(byte[] key, byte[]... values) {
		return CatRedisSupport.execute(RedisCommand.PFADD, key, () -> connection.pfAdd(key, values));
	}

	@Override
	public Long pfCount(byte[]... keys) {
		return CatRedisSupport.execute(RedisCommand.PFCOUNT, keys, () -> connection.pfCount(keys));
	}

	@Override
	public void pfMerge(byte[] destinationKey, byte[]... sourceKeys) {
		CatRedisSupport.execute(RedisCommand.PFMERGE, destinationKey, () -> connection.pfMerge(destinationKey, sourceKeys));
	}

	@Override
	public Long exists(byte[]... keys) {
		return CatRedisSupport.execute(RedisCommand.EXISTS, keys, () -> connection.exists(keys));
	}

	@Override
	public Long del(byte[]... keys) {
		return CatRedisSupport.execute(RedisCommand.DEL, keys, () -> connection.del(keys));
	}

	@Override
	public Long unlink(byte[]... keys) {
		return CatRedisSupport.execute(RedisCommand.UNLINK, keys, () -> connection.unlink(keys));
	}

	@Override
	public DataType type(byte[] key) {
		return CatRedisSupport.execute(RedisCommand.TYPE, key, () -> connection.type(key));
	}

	@Override
	public Long touch(byte[]... keys) {
		return CatRedisSupport.execute(RedisCommand.TOUCH, keys, () -> connection.touch(keys));
	}

	@Override
	public Set<byte[]> keys(byte[] pattern) {
		return CatRedisSupport.execute(RedisCommand.KEYS, pattern, () -> connection.keys(pattern));
	}

	@Override
	public Cursor<byte[]> scan(ScanOptions options) {
		return CatRedisSupport.execute(RedisCommand.SCAN, () -> connection.scan(options));
	}

	@Override
	public byte[] randomKey() {
		return CatRedisSupport.execute(RedisCommand.RANDOMKEY, () -> connection.randomKey());
	}

	@Override
	public void rename(byte[] oldName, byte[] newName) {
		CatRedisSupport.execute(RedisCommand.RENAME, () -> connection.rename(oldName, newName));
	}

	@Override
	public Boolean renameNX(byte[] oldName, byte[] newName) {
		return CatRedisSupport.execute(RedisCommand.RENAMENX, () -> connection.renameNX(oldName, newName));
	}

	@Override
	public Boolean expire(byte[] key, long seconds) {
		return CatRedisSupport.execute(RedisCommand.EXPIRE, key, () -> connection.expire(key, seconds));
	}

	@Override
	public Boolean pExpire(byte[] key, long millis) {
		return CatRedisSupport.execute(RedisCommand.PEXPIRE, key, () -> connection.pExpire(key, millis));
	}

	@Override
	public Boolean expireAt(byte[] key, long unixTime) {
		return CatRedisSupport.execute(RedisCommand.EXPIREAT, key, () -> connection.expireAt(key, unixTime));
	}

	@Override
	public Boolean pExpireAt(byte[] key, long unixTimeInMillis) {
		return CatRedisSupport.execute(RedisCommand.PEXPIREAT, key, () -> connection.pExpireAt(key, unixTimeInMillis));
	}

	@Override
	public Boolean persist(byte[] key) {
		return CatRedisSupport.execute(RedisCommand.PERSIST, key, () -> connection.persist(key));
	}

	@Override
	public Boolean move(byte[] key, int dbIndex) {
		return CatRedisSupport.execute(RedisCommand.MOVE, key, () -> connection.move(key, dbIndex));
	}

	@Override
	public Long ttl(byte[] key) {
		return CatRedisSupport.execute(RedisCommand.TTL, key, () -> connection.ttl(key));
	}

	@Override
	public Long ttl(byte[] key, TimeUnit timeUnit) {
		return CatRedisSupport.execute(RedisCommand.TTL, key, () -> connection.ttl(key, timeUnit));
	}

	@Override
	public Long pTtl(byte[] key) {
		return CatRedisSupport.execute(RedisCommand.PTTL, key, () -> connection.pTtl(key));
	}

	@Override
	public Long pTtl(byte[] key, TimeUnit timeUnit) {
		return CatRedisSupport.execute(RedisCommand.PTTL, key, () -> connection.pTtl(key, timeUnit));
	}

	@Override
	public List<byte[]> sort(byte[] key, SortParameters params) {
		return CatRedisSupport.execute(RedisCommand.SORT, key, () -> connection.sort(key, params));
	}

	@Override
	public Long sort(byte[] key, SortParameters params, byte[] storeKey) {
		return CatRedisSupport.execute(RedisCommand.SORT, key, () -> connection.sort(key, params, storeKey));
	}

	@Override
	public byte[] dump(byte[] key) {
		return CatRedisSupport.execute(RedisCommand.DUMP, key, () -> connection.dump(key));
	}

	@Override
	public void restore(byte[] key, long ttlInMillis, byte[] serializedValue) {
		CatRedisSupport.execute(RedisCommand.RESTORE, key, () -> connection.restore(key, ttlInMillis, serializedValue));
	}

	@Override
	public void restore(byte[] key, long ttlInMillis, byte[] serializedValue, boolean replace) {
		CatRedisSupport.execute(RedisCommand.RESTORE, key, () -> connection.restore(key, ttlInMillis, serializedValue, replace));
	}

	@Override
	public ValueEncoding encodingOf(byte[] key) {
		return CatRedisSupport.execute(RedisCommand.ENCODING, key, () -> connection.encodingOf(key));
	}

	@Override
	public Duration idletime(byte[] key) {
		return CatRedisSupport.execute(RedisCommand.IDLETIME, key, () -> connection.idletime(key));
	}

	@Override
	public Long refcount(byte[] key) {
		return CatRedisSupport.execute(RedisCommand.REFCOUNT, key, () -> connection.refcount(key));
	}

	@Override
	public Long rPush(byte[] key, byte[]... values) {
		return CatRedisSupport.execute(RedisCommand.RPUSH, key, () -> connection.rPush(key, values));
	}

	@Override
	public Long lPush(byte[] key, byte[]... values) {
		return CatRedisSupport.execute(RedisCommand.LPUSH, key, () -> connection.lPush(key, values));
	}

	@Override
	public Long rPushX(byte[] key, byte[] value) {
		return CatRedisSupport.execute(RedisCommand.RPUSHX, key, () -> connection.rPushX(key, value));
	}

	@Override
	public Long lPushX(byte[] key, byte[] value) {
		return CatRedisSupport.execute(RedisCommand.LPUSHX, key, () -> connection.lPushX(key, value));
	}

	@Override
	public Long lLen(byte[] key) {
		return CatRedisSupport.execute(RedisCommand.LLEN, key, () -> connection.lLen(key));
	}

	@Override
	public List<byte[]> lRange(byte[] key, long start, long end) {
		return CatRedisSupport.execute(RedisCommand.LRANGE, key, () -> connection.lRange(key, start, end));
	}

	@Override
	public void lTrim(byte[] key, long start, long end) {
		CatRedisSupport.execute(RedisCommand.LTRIM, key, () -> connection.lTrim(key, start, end));
	}

	@Override
	public byte[] lIndex(byte[] key, long index) {
		return CatRedisSupport.execute(RedisCommand.LINDEX, key, () -> connection.lIndex(key, index));
	}

	@Override
	public Long lInsert(byte[] key, Position where, byte[] pivot, byte[] value) {
		return CatRedisSupport.execute(RedisCommand.LINSERT, key, () -> connection.lInsert(key, where, pivot, value));
	}

	@Override
	public void lSet(byte[] key, long index, byte[] value) {
		CatRedisSupport.execute(RedisCommand.LSET, key, () -> connection.lSet(key, index, value));
	}

	@Override
	public Long lRem(byte[] key, long count, byte[] value) {
		return CatRedisSupport.execute(RedisCommand.LREM, key, () -> connection.lRem(key, count, value));
	}

	@Override
	public byte[] lPop(byte[] key) {
		return CatRedisSupport.execute(RedisCommand.LPOP, key, () -> connection.lPop(key));
	}

	@Override
	public byte[] rPop(byte[] key) {
		return CatRedisSupport.execute(RedisCommand.RPOP, key, () -> connection.rPop(key));
	}

	@Override
	public List<byte[]> bLPop(int timeout, byte[]... keys) {
		return CatRedisSupport.execute(RedisCommand.BLPOP, keys, () -> connection.bLPop(timeout, keys));
	}

	@Override
	public List<byte[]> bRPop(int timeout, byte[]... keys) {
		return CatRedisSupport.execute(RedisCommand.RPOPLPUSH, keys, () -> connection.bRPop(timeout, keys));
	}

	@Override
	public byte[] rPopLPush(byte[] srcKey, byte[] dstKey) {
		return CatRedisSupport.execute(RedisCommand.RPOPLPUSH, srcKey, () -> connection.rPopLPush(srcKey, dstKey));
	}

	@Override
	public byte[] bRPopLPush(int timeout, byte[] srcKey, byte[] dstKey) {
		return CatRedisSupport.execute(RedisCommand.BRPOPLPUSH, srcKey, () -> connection.bRPopLPush(timeout, srcKey, dstKey));
	}

	@Override
	public boolean isSubscribed() {
		return connection.isSubscribed();
	}

	@Override
	public Subscription getSubscription() {
		return connection.getSubscription();
	}

	@Override
	public Long publish(byte[] channel, byte[] message) {
		return CatRedisSupport.execute(RedisCommand.PUBLISH, () -> connection.publish(channel, message));
	}

	@Override
	public void subscribe(MessageListener listener, byte[]... channels) {
		CatRedisSupport.execute(RedisCommand.SUBSCRIBE, () -> connection.subscribe(listener, channels));
	}

	@Override
	public void pSubscribe(MessageListener listener, byte[]... patterns) {
		CatRedisSupport.execute(RedisCommand.PSUBSCRIBE, () -> connection.pSubscribe(listener, patterns));
	}

	@Override
	public void scriptFlush() {
		CatRedisSupport.execute(RedisCommand.SCRIPT_FLUSH, () -> connection.scriptFlush());
	}

	@Override
	public void scriptKill() {
		CatRedisSupport.execute(RedisCommand.SCRIPT_KILL, () -> connection.scriptKill());
	}

	@Override
	public String scriptLoad(byte[] script) {
		return CatRedisSupport.execute(RedisCommand.SCRIPT_LOAD, () -> connection.scriptLoad(script));
	}

	@Override
	public List<Boolean> scriptExists(String... scriptShas) {
		return CatRedisSupport.execute(RedisCommand.SCRIPT_EXISTS, () -> connection.scriptExists(scriptShas));
	}

	@Override
	public <T> T eval(byte[] script, ReturnType returnType, int numKeys, byte[]... keysAndArgs) {
		return CatRedisSupport.execute(RedisCommand.EVAL, () -> connection.eval(script, returnType, numKeys, keysAndArgs));
	}

	@Override
	public <T> T evalSha(String scriptSha, ReturnType returnType, int numKeys, byte[]... keysAndArgs) {
		return CatRedisSupport.execute(RedisCommand.EVALSHA, () -> connection.evalSha(scriptSha, returnType, numKeys, keysAndArgs));
	}

	@Override
	public <T> T evalSha(byte[] scriptSha, ReturnType returnType, int numKeys, byte[]... keysAndArgs) {
		return CatRedisSupport.execute(RedisCommand.EVALSHA, () -> connection.evalSha(scriptSha, returnType, numKeys, keysAndArgs));
	}

	@Override
	public void bgReWriteAof() {
		CatRedisSupport.execute(RedisCommand.BGREWRITEAOF, () -> connection.bgReWriteAof());
	}

	@Override
	public void bgSave() {
		CatRedisSupport.execute(RedisCommand.BGSAVE, () -> connection.bgSave());
	}

	@Override
	public Long lastSave() {
		return CatRedisSupport.execute(RedisCommand.LASTSAVE, () -> connection.lastSave());
	}

	@Override
	public void save() {
		CatRedisSupport.execute(RedisCommand.SAVE, () -> connection.save());
	}

	@Override
	public Long dbSize() {
		return CatRedisSupport.execute(RedisCommand.DBSIZE, () -> connection.dbSize());
	}

	@Override
	public void flushDb() {
		CatRedisSupport.execute(RedisCommand.FLUSHDB, () -> connection.flushDb());
	}

	@Override
	public void flushAll() {
		CatRedisSupport.execute(RedisCommand.FLUSHALL, () -> connection.flushAll());
	}

	@Override
	public Properties info() {
		return CatRedisSupport.execute(RedisCommand.INFO, () -> connection.info());
	}

	@Override
	public Properties info(String section) {
		return CatRedisSupport.execute(RedisCommand.INFO, () -> connection.info(section));
	}

	@Override
	public void shutdown() {
		CatRedisSupport.execute(RedisCommand.SHUTDOWN, () -> connection.shutdown());
	}

	@Override
	public void shutdown(ShutdownOption shutdownOption) {
		CatRedisSupport.execute(RedisCommand.SHUTDOWN, () -> connection.shutdown(shutdownOption));
	}

	@Override
	public Properties getConfig(String pattern) {
		return CatRedisSupport.execute(RedisCommand.CONFIG_GET, () -> connection.getConfig(pattern));
	}

	@Override
	public void setConfig(String param, String value) {
		CatRedisSupport.execute(RedisCommand.CONFIG_SET, () -> connection.setConfig(param, value));
	}

	@Override
	public void resetConfigStats() {
		CatRedisSupport.execute(RedisCommand.CONFIG_RESETSTAT, () -> connection.resetConfigStats());
	}

	@Override
	public Long time() {
		return CatRedisSupport.execute(RedisCommand.TIME, () -> connection.time());
	}

	@Override
	public void killClient(String host, int port) {
		CatRedisSupport.execute(RedisCommand.CLIENT_KILL, () -> connection.killClient(host, port));
	}

	@Override
	public void setClientName(byte[] name) {
		CatRedisSupport.execute(RedisCommand.CLIENT_SETNAME, () -> connection.setClientName(name));
	}

	@Override
	public String getClientName() {
		return CatRedisSupport.execute(RedisCommand.CLIENT_GETNAME, () -> connection.getClientName());
	}

	@Override
	public List<RedisClientInfo> getClientList() {
		return CatRedisSupport.execute(RedisCommand.CLIENT_LIST, () -> connection.getClientList());
	}

	@Override
	public void slaveOf(String host, int port) {
		CatRedisSupport.execute(RedisCommand.SLAVEOF, () -> connection.slaveOf(host, port));
	}

	@Override
	public void slaveOfNoOne() {
		CatRedisSupport.execute(RedisCommand.SLAVEOFNOONE, () -> connection.slaveOfNoOne());
	}

	@Override
	public void migrate(byte[] key, RedisNode target, int dbIndex, MigrateOption option) {
		CatRedisSupport.execute(RedisCommand.MIGRATE, key, () -> connection.migrate(key, target, dbIndex, option));
	}

	@Override
	public void migrate(byte[] key, RedisNode target, int dbIndex, MigrateOption option, long timeout) {
		CatRedisSupport.execute(RedisCommand.MIGRATE, key, () -> connection.migrate(key, target, dbIndex, option, timeout));
	}

	@Override
	public Long sAdd(byte[] key, byte[]... values) {
		return CatRedisSupport.execute(RedisCommand.SADD, key, () -> connection.sAdd(key, values));
	}

	@Override
	public Long sRem(byte[] key, byte[]... values) {
		return CatRedisSupport.execute(RedisCommand.SREM, key, () -> connection.sRem(key, values));
	}

	@Override
	public byte[] sPop(byte[] key) {
		return CatRedisSupport.execute(RedisCommand.SPOP, key, () -> connection.sPop(key));
	}

	@Override
	public List<byte[]> sPop(byte[] key, long count) {
		return CatRedisSupport.execute(RedisCommand.SPOP, key, () -> connection.sPop(key, count));
	}

	@Override
	public Boolean sMove(byte[] srcKey, byte[] destKey, byte[] value) {
		return CatRedisSupport.execute(RedisCommand.SMOVE, srcKey, () -> connection.sMove(srcKey, destKey, value));
	}

	@Override
	public Long sCard(byte[] key) {
		return CatRedisSupport.execute(RedisCommand.SCARD, key, () -> connection.sCard(key));
	}

	@Override
	public Boolean sIsMember(byte[] key, byte[] value) {
		return CatRedisSupport.execute(RedisCommand.SISMEMBER, key, () -> connection.sIsMember(key, value));
	}

	@Override
	public Set<byte[]> sInter(byte[]... keys) {
		return CatRedisSupport.execute(RedisCommand.SINTER, () -> connection.sInter(keys));
	}

	@Override
	public Long sInterStore(byte[] destKey, byte[]... keys) {
		return CatRedisSupport.execute(RedisCommand.SINTERSTORE, destKey, () -> connection.sInterStore(destKey, keys));
	}

	@Override
	public Set<byte[]> sUnion(byte[]... keys) {
		return CatRedisSupport.execute(RedisCommand.SUNION, () -> connection.sUnion(keys));
	}

	@Override
	public Long sUnionStore(byte[] destKey, byte[]... keys) {
		return CatRedisSupport.execute(RedisCommand.SUNIONSTORE, destKey, () -> connection.sUnionStore(destKey, keys));
	}

	@Override
	public Set<byte[]> sDiff(byte[]... keys) {
		return CatRedisSupport.execute(RedisCommand.SDIFF, () -> connection.sDiff(keys));
	}

	@Override
	public Long sDiffStore(byte[] destKey, byte[]... keys) {
		return CatRedisSupport.execute(RedisCommand.SDIFFSTORE, destKey, () -> connection.sDiffStore(destKey, keys));
	}

	@Override
	public Set<byte[]> sMembers(byte[] key) {
		return CatRedisSupport.execute(RedisCommand.SMEMBERS, key, () -> connection.sMembers(key));
	}

	@Override
	public byte[] sRandMember(byte[] key) {
		return CatRedisSupport.execute(RedisCommand.SRANDMEMBER, key, () -> connection.sRandMember(key));
	}

	@Override
	public List<byte[]> sRandMember(byte[] key, long count) {
		return CatRedisSupport.execute(RedisCommand.SRANDMEMBER, key, () -> connection.sRandMember(key, count));
	}

	@Override
	public Cursor<byte[]> sScan(byte[] key, ScanOptions scanOptions) {
		return CatRedisSupport.execute(RedisCommand.SSCAN, key, () -> connection.sScan(key, scanOptions));
	}

	@Override
	public byte[] get(byte[] key) {
		return CatRedisSupport.execute(RedisCommand.GET, key, () -> connection.get(key));
	}

	@Override
	public byte[] getSet(byte[] key, byte[] value) {
		return CatRedisSupport.execute(RedisCommand.GETSET, key, () -> connection.getSet(key, value));
	}

	@Override
	public List<byte[]> mGet(byte[]... keys) {
		return CatRedisSupport.execute(RedisCommand.MGET, keys, () -> connection.mGet(keys));
	}

	@Override
	public Boolean set(byte[] key, byte[] value) {
		return CatRedisSupport.execute(RedisCommand.SET, key, () -> connection.set(key, value));
	}

	@Override
	public Boolean set(byte[] key, byte[] value, Expiration expiration, SetOption option) {
		return CatRedisSupport.execute(RedisCommand.SET, key, () -> connection.set(key, value, expiration, option));
	}

	@Override
	public Boolean setNX(byte[] key, byte[] value) {
		return CatRedisSupport.execute(RedisCommand.SETNX, key, () -> connection.setNX(key, value));
	}

	@Override
	public Boolean setEx(byte[] key, long seconds, byte[] value) {
		return CatRedisSupport.execute(RedisCommand.SETEX, key, () -> connection.setEx(key, seconds, value));
	}

	@Override
	public Boolean pSetEx(byte[] key, long milliseconds, byte[] value) {
		return CatRedisSupport.execute(RedisCommand.PSETEX, key, () -> connection.setEx(key, milliseconds, value));
	}

	@Override
	public Boolean mSet(Map<byte[], byte[]> tuple) {
		return CatRedisSupport.execute(RedisCommand.MSET, () -> connection.mSet(tuple));
	}

	@Override
	public Boolean mSetNX(Map<byte[], byte[]> tuple) {
		return CatRedisSupport.execute(RedisCommand.MSETNX, () -> connection.mSetNX(tuple));
	}

	@Override
	public Long incr(byte[] key) {
		return CatRedisSupport.execute(RedisCommand.INCR, key, () -> connection.incr(key));
	}

	@Override
	public Long incrBy(byte[] key, long value) {
		return CatRedisSupport.execute(RedisCommand.INCRBY, key, () -> connection.incrBy(key, value));
	}

	@Override
	public Double incrBy(byte[] key, double value) {
		return CatRedisSupport.execute(RedisCommand.INCRBY, key, () -> connection.incrBy(key, value));
	}

	@Override
	public Long decr(byte[] key) {
		return CatRedisSupport.execute(RedisCommand.DECR, key, () -> connection.decr(key));
	}

	@Override
	public Long decrBy(byte[] key, long value) {
		return CatRedisSupport.execute(RedisCommand.DECRBY, key, () -> connection.decrBy(key, value));
	}

	@Override
	public Long append(byte[] key, byte[] value) {
		return CatRedisSupport.execute(RedisCommand.APPEND, key, () -> connection.append(key, value));
	}

	@Override
	public byte[] getRange(byte[] key, long begin, long end) {
		return CatRedisSupport.execute(RedisCommand.GETRANGE, key, () -> connection.getRange(key, begin, end));
	}

	@Override
	public void setRange(byte[] key, byte[] value, long offset) {
		CatRedisSupport.execute(RedisCommand.SETRANGE, key, () -> connection.setRange(key, value, offset));
	}

	@Override
	public Boolean getBit(byte[] key, long offset) {
		return CatRedisSupport.execute(RedisCommand.GETBIT, key, () -> connection.getBit(key, offset));
	}

	@Override
	public Boolean setBit(byte[] key, long offset, boolean value) {
		return CatRedisSupport.execute(RedisCommand.SETBIT, key, () -> connection.setBit(key, offset, value));
	}

	@Override
	public Long bitCount(byte[] key) {
		return CatRedisSupport.execute(RedisCommand.BITCOUNT, key, () -> connection.bitCount(key));
	}

	@Override
	public Long bitCount(byte[] key, long begin, long end) {
		return CatRedisSupport.execute(RedisCommand.BITCOUNT, key, () -> connection.bitCount(key, begin, end));
	}

	@Override
	public List<Long> bitField(byte[] key, BitFieldSubCommands bitFieldSubCommands) {
		return CatRedisSupport.execute(RedisCommand.BITFIELD, key, () -> connection.bitField(key, bitFieldSubCommands));
	}

	@Override
	public Long bitOp(BitOperation bitOperation, byte[] destination, byte[]... keys) {
		return CatRedisSupport.execute(RedisCommand.BITOP, keys, () -> connection.bitOp(bitOperation, destination, keys));
	}

	@Override
	public Long bitPos(byte[] key, boolean b, org.springframework.data.domain.Range<Long> range) {
		return CatRedisSupport.execute(RedisCommand.BITPOS, key, () -> connection.bitPos(key, b, range));
	}

	@Override
	public Long strLen(byte[] key) {
		return CatRedisSupport.execute(RedisCommand.STRLEN, key, () -> connection.strLen(key));
	}

	@Override
	public void multi() {
		CatRedisSupport.execute(RedisCommand.MULTI, () ->  connection.multi());
	}

	@Override
	public List<Object> exec() {
		return CatRedisSupport.execute(RedisCommand.EXEC, () -> connection.exec());
	}

	@Override
	public void discard() {
		CatRedisSupport.execute(RedisCommand.DISCARD, () ->  connection.discard());
	}

	@Override
	public void watch(byte[]... keys) {
		CatRedisSupport.execute(RedisCommand.WATCH, keys, () ->  connection.watch());
	}

	@Override
	public void unwatch() {
		CatRedisSupport.execute(RedisCommand.UNWATCH, () ->  connection.unwatch());
	}

	@Override
	public Boolean zAdd(byte[] key, double score, byte[] value) {
		return CatRedisSupport.execute(RedisCommand.ZADD, key, () -> connection.zAdd(key, score, value));
	}

	@Override
	public Long zAdd(byte[] key, Set<Tuple> tuples) {
		return CatRedisSupport.execute(RedisCommand.ZADD, key, () -> connection.zAdd(key, tuples));
	}

	@Override
	public Long zRem(byte[] key, byte[]... keys) {
		return CatRedisSupport.execute(RedisCommand.ZREM, key, () -> connection.zRem(key, keys));
	}

	@Override
	public Double zIncrBy(byte[] key, double increment, byte[] value) {
		return CatRedisSupport.execute(RedisCommand.ZINCRBY, key, () -> connection.zIncrBy(key, increment, value));
	}

	@Override
	public Long zRank(byte[] key, byte[] value) {
		return CatRedisSupport.execute(RedisCommand.ZRANK, key, () -> connection.zRank(key, value));
	}

	@Override
	public Long zRevRank(byte[] key, byte[] value) {
		return CatRedisSupport.execute(RedisCommand.ZREVRANK, key, () -> connection.zRevRank(key, value));
	}

	@Override
	public Set<byte[]> zRange(byte[] key, long start, long end) {
		return CatRedisSupport.execute(RedisCommand.ZRANGE, key, () -> connection.zRange(key, start, end));
	}

	@Override
	public Set<Tuple> zRangeWithScores(byte[] key, long start, long end) {
		return CatRedisSupport.execute(RedisCommand.ZRANGE_WITHSCORES, key, () -> connection.zRangeWithScores(key, start, end));
	}

	@Override
	public Set<Tuple> zRangeByScoreWithScores(byte[] key, Range range, Limit limit) {
		return CatRedisSupport.execute(RedisCommand.ZRANGEBYSCORE_WITHSCORES, key, () -> connection.zRangeByScoreWithScores(key, range, limit));
	}

	@Override
	public Set<byte[]> zRevRange(byte[] key, long start, long end) {
		return CatRedisSupport.execute(RedisCommand.ZREVRANGE, key, () -> connection.zRevRange(key, start, end));
	}

	@Override
	public Set<Tuple> zRevRangeWithScores(byte[] key, long start, long end) {
		return CatRedisSupport.execute(RedisCommand.ZREVRANGE_WITHSCORES, key, () -> connection.zRevRangeWithScores(key, start, end));
	}

	@Override
	public Set<byte[]> zRevRangeByScore(byte[] key, Range range, Limit limit) {
		return CatRedisSupport.execute(RedisCommand.ZREVRANGEBYSCORE, key, () -> connection.zRevRangeByScore(key, range, limit));
	}

	@Override
	public Set<Tuple> zRevRangeByScoreWithScores(byte[] key, Range range, Limit limit) {
		return CatRedisSupport.execute(RedisCommand.ZREVRANGEBYSCORE_WITHSCORES, key, () -> connection.zRevRangeByScoreWithScores(key, range, limit));
	}

	@Override
	public Long zCount(byte[] key, Range range) {
		return CatRedisSupport.execute(RedisCommand.ZCOUNT, key, () -> connection.zCount(key, range));
	}

	@Override
	public Long zCard(byte[] key) {
		return CatRedisSupport.execute(RedisCommand.ZCARD, key, () -> connection.zCard(key));
	}

	@Override
	public Double zScore(byte[] key, byte[] value) {
		return CatRedisSupport.execute(RedisCommand.ZSCORE, key, () -> connection.zScore(key, value));
	}

	@Override
	public Long zRemRange(byte[] key, long start, long end) {
		return CatRedisSupport.execute(RedisCommand.ZREMRANGE, key, () -> connection.zRemRange(key, start, end));
	}

	@Override
	public Long zRemRangeByScore(byte[] key, Range range) {
		return CatRedisSupport.execute(RedisCommand.ZREMRANGEBYSCORE, key, () -> connection.zRemRangeByScore(key, range));
	}

	@Override
	public Long zUnionStore(byte[] destKey, byte[]... sets) {
		return CatRedisSupport.execute(RedisCommand.ZUNIONSTORE, destKey, () -> connection.zUnionStore(destKey, sets));
	}

	@Override
	public Long zUnionStore(byte[] destKey, Aggregate aggregate, Weights weights, byte[]... sets) {
		return CatRedisSupport.execute(RedisCommand.ZUNIONSTORE, destKey, () -> connection.zUnionStore(destKey, aggregate, weights, sets));
	}

	@Override
	public Long zInterStore(byte[] destKey, byte[]... sets) {
		return CatRedisSupport.execute(RedisCommand.ZINTERSTORE, destKey, () -> connection.zInterStore(destKey, sets));
	}

	@Override
	public Long zInterStore(byte[] destKey, Aggregate aggregate, Weights weights, byte[]... sets) {
		return CatRedisSupport.execute(RedisCommand.ZINTERSTORE, destKey, () -> connection.zInterStore(destKey, aggregate, weights, sets));
	}

	@Override
	public Cursor<Tuple> zScan(byte[] key, ScanOptions scanOptions) {
		return CatRedisSupport.execute(RedisCommand.ZSCAN, key, () -> connection.zScan(key, scanOptions));
	}

	@Override
	public Set<byte[]> zRangeByScore(byte[] key, String min, String max, long offset, long count) {
		return CatRedisSupport.execute(RedisCommand.ZRANGEBYSCORE, key, () -> connection.zRangeByScore(key, min, max, offset, count));
	}

	@Override
	public Set<byte[]> zRangeByScore(byte[] key, Range range, Limit limit) {
		return CatRedisSupport.execute(RedisCommand.ZRANGEBYSCORE, key, () -> connection.zRangeByScore(key, range, limit));
	}

	@Override
	public Set<byte[]> zRangeByLex(byte[] key, Range range, Limit limit) {
		return CatRedisSupport.execute(RedisCommand.ZRANGEBYLEX, key, () -> connection.zRangeByLex(key, range, limit));
	}

	@Override
	public List<Long> lPos(byte[] key, byte[] element, Integer rank, Integer count) {
		return CatRedisSupport.execute(RedisCommand.LPOS, key, () -> connection.lPos(key, element, rank, count));
	}

	@Override
	public Long xAck(byte[] key, String group, RecordId... recordIds) {
		return CatRedisSupport.execute(RedisCommand.XACK, key, () -> connection.xAck(key, group, recordIds));
	}

	@Override
	public RecordId xAdd(MapRecord<byte[], byte[], byte[]> record, XAddOptions options) {
		return CatRedisSupport.execute(RedisCommand.XADD, () -> connection.xAdd(record, options));
	}

	@Override
	public List<RecordId> xClaimJustId(byte[] key, String group, String newOwner, XClaimOptions options) {
		return CatRedisSupport.execute(RedisCommand.XCLAIMJUSTID, key, () -> connection.xClaimJustId(key, group, newOwner, options));
	}

	@Override
	public List<ByteRecord> xClaim(byte[] key, String group, String newOwner, XClaimOptions options) {
		return CatRedisSupport.execute(RedisCommand.XCLAIM, () -> connection.xClaim(key, group, newOwner, options));
	}

	@Override
	public Long xDel(byte[] key, RecordId... recordIds) {
		return CatRedisSupport.execute(RedisCommand.XDEL, () -> connection.xDel(key, recordIds));
	}

	@Override
	public String xGroupCreate(byte[] key, String groupName, ReadOffset readOffset) {
		return CatRedisSupport.execute(RedisCommand.XGROUPCREATE, () -> connection.xGroupCreate(key, groupName, readOffset));
	}

	@Override
	public String xGroupCreate(byte[] key, String groupName, ReadOffset readOffset, boolean mkStream) {
		return CatRedisSupport.execute(RedisCommand.XGROUPCREATE, () -> connection.xGroupCreate(key, groupName, readOffset, mkStream));
	}

	@Override
	public Boolean xGroupDelConsumer(byte[] key, Consumer consumer) {
		return CatRedisSupport.execute(RedisCommand.XGROUPDELCONSUMER, () -> connection.xGroupDelConsumer(key, consumer));
	}

	@Override
	public Boolean xGroupDestroy(byte[] key, String groupName) {
		return CatRedisSupport.execute(RedisCommand.XGROUPDESTROY, () -> connection.xGroupDestroy(key, groupName));
	}

	@Override
	public StreamInfo.XInfoStream xInfo(byte[] key) {
		return CatRedisSupport.execute(RedisCommand.XINFOSTREAM, () -> connection.xInfo(key));
	}

	@Override
	public StreamInfo.XInfoGroups xInfoGroups(byte[] key) {
		return CatRedisSupport.execute(RedisCommand.XINFOGROUPS, () -> connection.xInfoGroups(key));
	}

	@Override
	public StreamInfo.XInfoConsumers xInfoConsumers(byte[] key, String groupName) {
		return CatRedisSupport.execute(RedisCommand.XINFOCONSUMERS, () -> connection.xInfoConsumers(key, groupName));
	}

	@Override
	public Long xLen(byte[] key) {
		return CatRedisSupport.execute(RedisCommand.XLEN, () -> connection.xLen(key));
	}

	@Override
	public PendingMessagesSummary xPending(byte[] key, String groupName) {
		return CatRedisSupport.execute(RedisCommand.XPENDING, () -> connection.xPending(key, groupName));
	}

	@Override
	public PendingMessages xPending(byte[] key, String groupName, XPendingOptions options) {
		return CatRedisSupport.execute(RedisCommand.XPENDING, () -> connection.xPending(key, groupName, options));
	}

	@Override
	public List<ByteRecord> xRange(byte[] key, org.springframework.data.domain.Range<String> range, Limit limit) {
		return CatRedisSupport.execute(RedisCommand.XRANGE, () -> connection.xRange(key, range, limit));
	}

	@Override
	public List<ByteRecord> xRead(StreamReadOptions readOptions, StreamOffset<byte[]>... streams) {
		return CatRedisSupport.execute(RedisCommand.XREAD, () -> connection.xRead(readOptions, streams));
	}

	@Override
	public List<ByteRecord> xReadGroup(Consumer consumer, StreamReadOptions readOptions, StreamOffset<byte[]>... streams) {
		return CatRedisSupport.execute(RedisCommand.XREADGROUP, () -> connection.xReadGroup(consumer, readOptions, streams));
	}

	@Override
	public List<ByteRecord> xRevRange(byte[] key, org.springframework.data.domain.Range<String> range, Limit limit) {
		return CatRedisSupport.execute(RedisCommand.XREVRANGE, () -> connection.xRevRange(key, range, limit));
	}

	@Override
	public Long xTrim(byte[] key, long count) {
		return CatRedisSupport.execute(RedisCommand.XTRIM, () -> connection.xTrim(key, count));
	}

	@Override
	public Long xTrim(byte[] key, long count, boolean approximateTrimming) {
		return CatRedisSupport.execute(RedisCommand.XTRIM, () -> connection.xTrim(key, count, approximateTrimming));
	}

	@Override
	public Long zLexCount(byte[] key, Range range) {
		return CatRedisSupport.execute(RedisCommand.ZLEXCOUNT, () -> connection.zLexCount(key, range));
	}

	@Override
	public Set<byte[]> zRevRangeByLex(byte[] key, Range range, Limit limit) {
		return CatRedisSupport.execute(RedisCommand.ZREVRANGEBYLEX, () -> connection.zRevRangeByLex(key, range, limit));
	}
}
