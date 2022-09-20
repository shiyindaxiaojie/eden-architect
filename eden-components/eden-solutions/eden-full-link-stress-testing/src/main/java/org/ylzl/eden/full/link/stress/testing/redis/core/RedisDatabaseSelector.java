package org.ylzl.eden.full.link.stress.testing.redis.core;

import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * Redis 数据库选择器
 *
 * @author <a href="mailto:guoyuanlu@puyiwm.com">gyl</a>
 * @since 1.0.0
 */
public class RedisDatabaseSelector {

	private static final TransmittableThreadLocal<Integer> CONTEXT = new TransmittableThreadLocal<>();

	public static void select(int db) {
		CONTEXT.set(db);
	}

	public static Integer get() {
		return CONTEXT.get();
	}
}
