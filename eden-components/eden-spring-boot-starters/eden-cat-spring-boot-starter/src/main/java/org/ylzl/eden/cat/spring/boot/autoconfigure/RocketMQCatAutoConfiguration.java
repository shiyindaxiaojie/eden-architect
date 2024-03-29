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

package org.ylzl.eden.cat.spring.boot.autoconfigure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.spring.autoconfigure.RocketMQAutoConfiguration;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.ylzl.eden.spring.framework.beans.ApplicationContextHelper;
import org.ylzl.eden.spring.integration.cat.integration.rocketmq.RocketMQCatConsumeMessageHook;
import org.ylzl.eden.spring.integration.cat.integration.rocketmq.RocketMQCatSendMessageHook;

/**
 * RocketMQ 集成 CAT 自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ConditionalOnExpression("${cat.rocketmq.enabled:true}")
@ConditionalOnBean({
	CatAutoConfiguration.class,
	RocketMQAutoConfiguration.class
})
@AutoConfigureAfter(CatAutoConfiguration.class)
@RequiredArgsConstructor
@Slf4j
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Configuration(proxyBeanMethods = false)
public class RocketMQCatAutoConfiguration implements InitializingBean {

	private static final String AUTOWIRED_ROCKET_MQ_CAT_SEND_MESSAGE_HOOK = "Autowired RocketMQCatSendMessageHook";

	private static final String AUTOWIRED_ROCKET_MQ_CAT_CONSUME_MESSAGE_HOOK = "Autowired RocketMQCatConsumeMessageHook";

	@Override
	public void afterPropertiesSet() {
		DefaultMQProducer mqProducer = ApplicationContextHelper.getBean(DefaultMQProducer.class);
		if (mqProducer != null) {
			log.debug(AUTOWIRED_ROCKET_MQ_CAT_SEND_MESSAGE_HOOK);
			mqProducer.getDefaultMQProducerImpl().registerSendMessageHook(new RocketMQCatSendMessageHook());
		}

		DefaultMQPushConsumer mqConsumer = ApplicationContextHelper.getBean(DefaultMQPushConsumer.class);
		if (mqConsumer != null) {
			log.debug(AUTOWIRED_ROCKET_MQ_CAT_CONSUME_MESSAGE_HOOK);
			mqConsumer.getDefaultMQPushConsumerImpl().registerConsumeMessageHook(new RocketMQCatConsumeMessageHook());
		}
	}
}
