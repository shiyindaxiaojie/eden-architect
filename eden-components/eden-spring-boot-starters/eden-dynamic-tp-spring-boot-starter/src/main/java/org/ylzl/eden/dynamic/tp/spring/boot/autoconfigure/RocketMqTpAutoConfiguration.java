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

package org.ylzl.eden.dynamic.tp.spring.boot.autoconfigure;

import com.dtp.adapter.rocketmq.RocketMqDtpAdapter;
import com.dtp.starter.common.autoconfigure.BaseBeanAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Role;
import org.ylzl.eden.common.mq.integration.rocketmq.RocketMQConsumer;
import org.ylzl.eden.dynamic.tp.spring.boot.adapter.CustomRocketMqDtpAdapter;
import org.ylzl.eden.spring.boot.bootstrap.constant.Conditions;

/**
 * 自定义 RocketMqDtpAdapter 装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@ConditionalOnProperty(
    prefix = "spring.dynamic.tp",
    name = Conditions.ENABLED,
    havingValue = Conditions.TRUE,
    matchIfMissing = true
)
@ConditionalOnBean(RocketMQConsumer.class)
@AutoConfigureAfter({BaseBeanAutoConfiguration.class})
@Slf4j
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Configuration(proxyBeanMethods = false)
public class RocketMqTpAutoConfiguration {

	private static final String AUTOWIRED_CUSTOM_ROCKET_MQ_DTP_ADAPTER = "Autowired CustomRocketMqDtpAdapter";

	@Primary
    @Bean
    public RocketMqDtpAdapter rocketMqDtpHandler() {
        log.debug(AUTOWIRED_CUSTOM_ROCKET_MQ_DTP_ADAPTER);
		return new CustomRocketMqDtpAdapter();
    }
}
