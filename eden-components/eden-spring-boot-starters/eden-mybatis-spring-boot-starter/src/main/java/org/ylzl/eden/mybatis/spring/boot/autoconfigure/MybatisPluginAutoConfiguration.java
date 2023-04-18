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

package org.ylzl.eden.mybatis.spring.boot.autoconfigure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.ylzl.eden.mybatis.spring.boot.env.MybatisPluginProperties;
import org.ylzl.eden.spring.data.mybatis.plugin.MybatisSqlLogInterceptor;

/**
 * Mybatis 插件扩展自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@ConditionalOnBean(SqlSessionFactory.class)
@ConditionalOnProperty(name = MybatisPluginProperties.SQL_LOG_ENABLED)
@EnableConfigurationProperties({MybatisPluginProperties.class})
@RequiredArgsConstructor
@Slf4j
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Configuration(proxyBeanMethods = false)
public class MybatisPluginAutoConfiguration {

	public static final String AUTOWIRED_MYBATIS_SQL_LOG_INTERCEPTOR = "Autowired MybatisSqlLogInterceptor";

	private final MybatisPluginProperties mybatisPluginProperties;

	@Bean
	public MybatisSqlLogInterceptor mybatisSqlLogInterceptor() {
		log.debug(AUTOWIRED_MYBATIS_SQL_LOG_INTERCEPTOR);
		MybatisSqlLogInterceptor interceptor = new MybatisSqlLogInterceptor();
		interceptor.setSlownessThreshold(mybatisPluginProperties.getSqlLog().getSlownessThreshold());
		return interceptor;
	}
}
