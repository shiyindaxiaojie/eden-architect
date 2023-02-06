/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ylzl.eden.common.mq.spring.boot.env.convertor;

import org.apache.rocketmq.spring.autoconfigure.RocketMQProperties;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.ylzl.eden.common.mq.integration.rocketmq.config.RocketMQConfig;
import org.ylzl.eden.common.mq.spring.boot.env.RocketMQConsumerProperties;
import org.ylzl.eden.common.mq.spring.boot.env.RocketMQProducerProperties;

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
