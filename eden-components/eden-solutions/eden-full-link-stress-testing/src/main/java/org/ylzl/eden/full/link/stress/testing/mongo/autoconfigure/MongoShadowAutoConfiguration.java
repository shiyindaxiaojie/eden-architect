package org.ylzl.eden.full.link.stress.testing.mongo.autoconfigure;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoClientFactory;
import org.springframework.boot.autoconfigure.mongo.MongoClientSettingsBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.full.link.stress.testing.mongo.env.MongoShadowProperties;

import java.util.stream.Collectors;

/**
 * TODO
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@AutoConfigureAfter(MongoAutoConfiguration.class)
@EnableConfigurationProperties(MongoShadowProperties.class)
@RequiredArgsConstructor
@Slf4j
@Configuration(proxyBeanMethods = false)
public class MongoShadowAutoConfiguration {

	private final MongoShadowProperties mongoShadowProperties;

	@
	@Bean
	public MongoClient mongoClient(ObjectProvider<MongoClientSettingsBuilderCustomizer> builderCustomizers,
							 MongoClientSettings settings) {
		return new MongoClientFactory(builderCustomizers.orderedStream().collect(Collectors.toList()))
			.createMongoClient(settings);
	}
}
