package org.ylzl.eden.spring.data.redis.core;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 动态 StringRedisTemplate
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class DynamicRedisTemplate<K, V> extends RedisTemplate<K, V> {

	@NotNull
	@Override
	protected RedisConnection preProcessConnection(RedisConnection connection, boolean existingConnection) {
		Integer db = RedisDatabaseSelector.get();
		if (db != null) {
			connection.select(db);
		}
		return super.preProcessConnection(connection, existingConnection);
	}

	@NotNull
	@Override
	protected RedisConnection createRedisConnectionProxy(RedisConnection pm) {
		return super.createRedisConnectionProxy(pm);
	}
}
