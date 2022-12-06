package org.ylzl.eden.dynamic.mq.spring.boot.env.convertor;

import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import org.ylzl.eden.dynamic.mq.integration.rocketmq.config.RocketMQProducerConfig;
import org.ylzl.eden.dynamic.mq.spring.boot.env.RocketMQProducerProperties;

/**
 * RocketMQ 消费者配置转换器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
	nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RocketMQProducerConvertor {

	RocketMQProducerConvertor INSTANCE = Mappers.getMapper(RocketMQProducerConvertor.class);

	RocketMQProducerConfig toConfig(RocketMQProducerProperties properties);
}
