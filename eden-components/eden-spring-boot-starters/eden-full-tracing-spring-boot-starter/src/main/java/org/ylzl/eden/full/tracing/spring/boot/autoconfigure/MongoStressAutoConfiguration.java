package org.ylzl.eden.full.tracing.spring.boot.autoconfigure;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoClientFactory;
import org.springframework.boot.autoconfigure.mongo.MongoClientSettingsBuilderCustomizer;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.ylzl.eden.full.tracing.integration.mongo.MongoShadowAspect;
import org.ylzl.eden.full.tracing.spring.boot.env.MongoShadowProperties;
import org.ylzl.eden.spring.data.mongodb.core.DynamicMongoTemplate;

import java.util.stream.Collectors;

/**
 * MongoDB 压测标记自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ConditionalOnProperty(value = MongoStressAutoConfiguration.ENABLED, havingValue = "true")
@AutoConfigureAfter(MongoAutoConfiguration.class)
@EnableConfigurationProperties(MongoShadowProperties.class)
@RequiredArgsConstructor
@Slf4j
@Configuration(proxyBeanMethods = false)
public class MongoStressAutoConfiguration {

	public static final String ENABLED ="stress.mongodb.enabled";

	public static final String MONGO_CLIENT = "mongoCLient";

	public static final String SHADOW_MONGO_CLIENT = "shadowMongoClient";

	public static final String MONGO_DATABASE_FACTORY = "mongoDatabaseFactory";

	public static final String SHADOW_MONGO_DATABASE_FACTORY = "shadowMongoDatabaseFactory";

	private final MongoProperties mongoProperties;

	private final MongoShadowProperties mongoShadowProperties;

	@Bean(SHADOW_MONGO_CLIENT)
	public MongoClient shadowMongoClient(ObjectProvider<MongoClientSettingsBuilderCustomizer> builderCustomizers,
										 MongoClientSettings settings) {
		return new MongoClientFactory(builderCustomizers.orderedStream().collect(Collectors.toList()))
			.createMongoClient(settings);
	}

	@Bean(SHADOW_MONGO_DATABASE_FACTORY)
	public MongoDatabaseFactory shadowMongoDatabaseFactory(@Qualifier(SHADOW_MONGO_CLIENT) MongoClient mongoClient) {
		return new SimpleMongoClientDatabaseFactory(mongoClient, mongoShadowProperties.getUri());
	}

	@Primary
	@Bean(MONGO_CLIENT)
	public MongoClient mongoClient(ObjectProvider<MongoClientSettingsBuilderCustomizer> builderCustomizers,
								   MongoClientSettings settings) {
		return new MongoClientFactory(builderCustomizers.orderedStream().collect(Collectors.toList()))
			.createMongoClient(settings);
	}

	@Primary
	@Bean(MONGO_DATABASE_FACTORY)
	public MongoDatabaseFactory mongoDatabaseFactory(MongoClient mongoClient) {
		return new SimpleMongoClientDatabaseFactory(mongoClient, mongoProperties.getUri());
	}

	@Primary
	@Bean
	public MongoTemplate mongoTemplate(MongoDatabaseFactory mongoDatabaseFactory) {
		return new DynamicMongoTemplate(mongoDatabaseFactory);
	}

	@Bean
	public MongoShadowAspect mongoShadowAspect(@Qualifier(SHADOW_MONGO_DATABASE_FACTORY) MongoDatabaseFactory mongoDatabaseFactory) {
		return new MongoShadowAspect(mongoDatabaseFactory);
	}
}
