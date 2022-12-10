package org.ylzl.eden.dynamic.mq.spring.boot.env.convertor;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.ylzl.eden.dynamic.mq.integration.kafka.config.KafkaConfig;

/**
 * Kafka 配置转换器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
	nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface KafkaConvertor {

	KafkaConvertor INSTANCE = Mappers.getMapper(KafkaConvertor.class);

	KafkaConfig toConfig(KafkaProperties properties);

	void updateConfig(KafkaProperties properties, @MappingTarget KafkaConfig config);
}
