package org.ylzl.eden.spring.data.mongodb.core;

import com.mongodb.client.MongoDatabase;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * 动态 Mongo 操作模板
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class DynamicMongoTemplate extends MongoTemplate {

	public DynamicMongoTemplate(MongoDatabaseFactory factory) {
		super(factory);
	}

	@NotNull
	@Override
	public MongoDatabase getDb() {
		return MongoDatabaseSelector.get() == null? super.getDb() : MongoDatabaseSelector.get().getMongoDatabase();
	}
}
