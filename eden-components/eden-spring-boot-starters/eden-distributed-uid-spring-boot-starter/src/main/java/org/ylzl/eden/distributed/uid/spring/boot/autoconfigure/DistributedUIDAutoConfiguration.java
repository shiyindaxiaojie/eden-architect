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

package org.ylzl.eden.distributed.uid.spring.boot.autoconfigure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.ylzl.eden.distributed.uid.DistributedUID;
import org.ylzl.eden.distributed.uid.spring.boot.env.DistributedUIDProperties;
import org.ylzl.eden.distributed.uid.spring.boot.support.DistributedUIDHelper;
import org.ylzl.eden.spring.boot.bootstrap.constant.Conditions;

/**
 * 分布式唯一ID操作自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ConditionalOnProperty(
	prefix = DistributedUIDProperties.PREFIX,
	name = Conditions.ENABLED,
	havingValue = Conditions.TRUE
)
@ConditionalOnBean(DistributedUID.class)
@EnableConfigurationProperties(DistributedUIDProperties.class)
@RequiredArgsConstructor
@Slf4j
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Configuration(proxyBeanMethods = false)
public class DistributedUIDAutoConfiguration {

	public static final String AUTOWIRED_DISTRIBUTED_UID_HELPER = "Autowired DistributedUIDHelper";

	private final DistributedUIDProperties distributedUIDProperties;

	@Bean
	public DistributedUIDHelper distributedUIDHelper() {
		log.debug(AUTOWIRED_DISTRIBUTED_UID_HELPER);
		return new DistributedUIDHelper(distributedUIDProperties.getPrimary());
	}
}
