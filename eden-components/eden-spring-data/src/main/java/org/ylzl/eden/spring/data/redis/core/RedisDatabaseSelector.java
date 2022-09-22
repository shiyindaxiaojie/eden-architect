package org.ylzl.eden.spring.data.redis.core;

import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * Redis 数据库选择器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class RedisDatabaseSelector {

	private static final TransmittableThreadLocal<Integer> CONTEXT = new TransmittableThreadLocal<>();

	public static void set(int db) {
		CONTEXT.set(db);
	}

	public static Integer get() {
		return CONTEXT.get();
	}

	public static void remove() {
		CONTEXT.remove();
	}
}
