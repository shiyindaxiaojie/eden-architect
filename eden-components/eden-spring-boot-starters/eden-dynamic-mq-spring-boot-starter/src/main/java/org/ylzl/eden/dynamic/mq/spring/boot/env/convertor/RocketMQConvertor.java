package org.ylzl.eden.dynamic.mq.spring.boot.env.convertor;

import org.apache.rocketmq.spring.autoconfigure.RocketMQProperties;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.ylzl.eden.dynamic.mq.integration.rocketmq.config.RocketMQConfig;
import org.ylzl.eden.dynamic.mq.spring.boot.env.RocketMQConsumerProperties;
import org.ylzl.eden.dynamic.mq.spring.boot.env.RocketMQProducerProperties;

/**
 * RocketMQ 配置转换器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
	nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RocketMQConvertor {

	RocketMQConvertor INSTANCE = Mappers.getMapper(RocketMQConvertor.class);

	RocketMQConfig toConfig(RocketMQProperties properties);

	void updateConfigFromConsumer(RocketMQConsumerProperties properties, @MappingTarget RocketMQConfig.Consumer config);

	void updateConfigFromProducer(RocketMQProducerProperties properties, @MappingTarget RocketMQConfig.Producer config);
}
