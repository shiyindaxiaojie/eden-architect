package org.ylzl.eden.distributed.lock.integration.jedis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.ylzl.eden.commons.lang.StringUtils;
import org.ylzl.eden.distributed.lock.core.DistributedLock;
import org.ylzl.eden.distributed.lock.exception.DistributedLockAcquireException;
import org.ylzl.eden.distributed.lock.exception.DistributedLockReleaseException;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.commands.JedisCommands;
import redis.clients.jedis.params.SetParams;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Jedis 分布式锁
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Deprecated
@RequiredArgsConstructor
@Slf4j
public class JedisDistributedLock implements DistributedLock {

	private static final String UNLOCK_LUA =
		"if redis.call(\"get\",KEYS[1]) == ARGV[1] "
			+ "then return redis.call(\"del\",KEYS[1])"
			+ "else return 0 end";

	private final ThreadLocal<String> lock = new ThreadLocal<>();

	private final RedisTemplate<String, Object> redisTemplate;

	/**
	 * 阻塞加锁
	 *
	 * @param key 锁对象
	 */
	@Override
	public boolean lock(String key) {
		log.debug("Jedis create lock: {}", key);
		try {
			String result =
				redisTemplate.execute(
					(RedisCallback<String>) connection -> {
						JedisCommands commands = (JedisCommands) connection.getNativeConnection();
						String value = UUID.randomUUID().toString();
						lock.set(value);
						SetParams setParams = new SetParams();
						setParams.ex(-1);
						return commands.set(key, value, setParams);
					});
			return StringUtils.isNotEmpty(result);
		} catch (Exception e) {
			log.error("Jedis create lock: {}, catch exception: {}", key, e.getMessage(), e);
			throw new DistributedLockAcquireException(e);
		}
	}

	/**
	 * 加锁
	 *
	 * @param key      锁对象
	 * @param waitTime 等待时间
	 * @param timeUnit 时间单位
	 * @return
	 */
	@Override
	public boolean lock(String key, int waitTime, TimeUnit timeUnit) {
		log.warn("Jedis create lock: {}, not support waitTime", key);
		return lock(key);
	}

	/**
	 * 释放锁
	 *
	 * @param key 锁对象
	 */
	@Override
	public void unlock(String key) {
		log.debug("Jedis release lock: {}", key);
		try {
			final List<String> keys = Collections.singletonList(key);
			final List<String> args = Collections.singletonList(lock.get());
			Long result =
				redisTemplate.execute(
					(RedisCallback<Long>) connection -> { // 集群模式不支持执行 LUA 脚本
						Object nativeConnection = connection.getNativeConnection();
						if (nativeConnection instanceof JedisCluster) { // 集群模式
							return (Long) ((JedisCluster) nativeConnection).eval(UNLOCK_LUA, keys, args);
						}
						if (nativeConnection instanceof Jedis) {
							return (Long) ((Jedis) nativeConnection).eval(UNLOCK_LUA, keys, args);
						}
						return 0L;
					});

			if (result == null || result == 0L) {
				log.warn("Jedis release lock: {}, but it not work", key);
			}
		} catch (Exception e) {
			log.error("Jedis release lock: {}, catch exception: {}", key, e.getMessage(), e);
			throw new DistributedLockReleaseException(e);
		}
	}
}
