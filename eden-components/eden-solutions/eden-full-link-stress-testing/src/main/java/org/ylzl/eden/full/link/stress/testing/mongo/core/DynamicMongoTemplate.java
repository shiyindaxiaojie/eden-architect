package org.ylzl.eden.full.link.stress.testing.mongo.core;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.mongodb.client.MongoDatabase;
import org.springframework.data.mongodb.core.MongoDatabaseFactorySupport;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * 动态 Mongo 操作模板
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class DynamicMongoTemplate extends MongoTemplate {

	private static final TransmittableThreadLocal<MongoDatabaseFactorySupport> CONTEXT = new TransmittableThreadLocal<>();

	public DynamicMongoTemplate(MongoDatabaseFactorySupport support) {
		super(support);
	}

	public void setFactory(MongoDatabaseFactorySupport factory) {
		CONTEXT.set(factory);
	}

	public void removeFactory() {
		CONTEXT.remove();
	}

	@Override
	public MongoDatabase getDb() {
		return CONTEXT.get().getMongoDatabase();
	}
}
