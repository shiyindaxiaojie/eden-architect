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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.ylzl.eden.commons.net.IpConfigUtils;
import org.ylzl.eden.distributed.uid.SnowflakeGenerator;
import org.ylzl.eden.distributed.uid.SegmentGenerator;
import org.ylzl.eden.distributed.uid.config.SnowflakeGeneratorConfig;
import org.ylzl.eden.distributed.uid.integration.leaf.snowflake.model.App;
import org.ylzl.eden.distributed.uid.spring.boot.env.DistributedUIDProperties;
import org.ylzl.eden.distributed.uid.support.SnowflakeGeneratorHelper;
import org.ylzl.eden.distributed.uid.support.SegmentGeneratorHelper;
import org.ylzl.eden.spring.boot.bootstrap.constant.Conditions;
import org.ylzl.eden.spring.framework.bootstrap.constant.SpringProperties;

import javax.sql.DataSource;

/**
 * 分布式唯一ID操作自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@EnableConfigurationProperties(DistributedUIDProperties.class)
@RequiredArgsConstructor
@Slf4j
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Configuration(proxyBeanMethods = false)
public class DistributedUIDAutoConfiguration {

	private static final String AUTOWIRED_SNOWFLAKE_GENERATOR = "Autowired SnowflakeGenerator";

	private static final String AUTOWIRED_SEGMENT_GENERATOR = "Autowired SegmentGenerator";

	private final DistributedUIDProperties properties;

	@ConditionalOnProperty(
		prefix = DistributedUIDProperties.ID_GENERATOR_PREFIX,
		name = Conditions.ENABLED,
		havingValue = Conditions.FALSE
	)
	@ConditionalOnMissingBean
	@Bean
	public SnowflakeGenerator snowflakeGenerator(@Value(SpringProperties.NAME_PATTERN) String applicationName,
                                          ServerProperties serverProperties) {
		log.debug(AUTOWIRED_SNOWFLAKE_GENERATOR);
		SnowflakeGeneratorConfig config = properties.getSnowflakeGenerator();
		config.setName(applicationName);
		return SnowflakeGeneratorHelper.snowflakeGenerator(properties.getSegmentGenerator().getType(),
			App.builder().ip(IpConfigUtils.getIpAddress()).port(serverProperties.getPort()).build(), config);
	}

	@ConditionalOnProperty(
		prefix = DistributedUIDProperties.SEGMENT_GENERATOR_PREFIX,
		name = Conditions.ENABLED,
		havingValue = Conditions.FALSE
	)
	@ConditionalOnMissingBean
	@Bean
	public SegmentGenerator segmentGenerator(DataSource dataSource) {
		log.debug(AUTOWIRED_SEGMENT_GENERATOR);
		return SegmentGeneratorHelper.segmentGenerator(properties.getSegmentGenerator().getType(),
			dataSource,
			properties.getSegmentGenerator());
	}
}
