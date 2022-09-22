package org.ylzl.eden.spring.data.mongodb.core;

import com.alibaba.ttl.TransmittableThreadLocal;
import org.springframework.data.mongodb.MongoDatabaseFactory;

/**
 * MongoDB 数据库选择器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class MongoDatabaseSelector {

	private static final TransmittableThreadLocal<MongoDatabaseFactory> CONTEXT = new TransmittableThreadLocal<>();

	public static void set(MongoDatabaseFactory factory) {
		CONTEXT.set(factory);
	}

	public static MongoDatabaseFactory get() {
		return CONTEXT.get();
	}

	public static void remove() {
		CONTEXT.remove();
	}
}
