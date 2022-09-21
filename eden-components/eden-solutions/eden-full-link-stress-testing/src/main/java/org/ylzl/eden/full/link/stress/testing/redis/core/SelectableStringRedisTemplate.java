package org.ylzl.eden.full.link.stress.testing.redis.core;

import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * 可选择的 StringRedisTemplate
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class SelectableStringRedisTemplate extends StringRedisTemplate {

	@Override
	protected RedisConnection preProcessConnection(RedisConnection connection, boolean existingConnection) {
		Integer db = RedisDatabaseSelector.get();
		if (db != null) {
			connection.select(db);
		}
		return super.preProcessConnection(connection, existingConnection);
	}

	@Override
	protected RedisConnection createRedisConnectionProxy(RedisConnection pm) {
		return super.createRedisConnectionProxy(pm);
	}
}
