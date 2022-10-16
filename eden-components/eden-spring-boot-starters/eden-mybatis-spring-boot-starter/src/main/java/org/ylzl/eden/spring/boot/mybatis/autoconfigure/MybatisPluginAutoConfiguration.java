/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ylzl.eden.spring.boot.mybatis.autoconfigure;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.spring.boot.mybatis.env.MybatisPluginProperties;
import org.ylzl.eden.spring.data.mybatis.plugin.MybatisSqlLogInterceptor;

import java.util.List;

/**
 * Mybatis 插件扩展自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@ConditionalOnClass({
	SqlSessionFactory.class,
	SqlSessionFactoryBean.class,
	MybatisConfiguration.class
})
@ConditionalOnProperty(name = MybatisPluginProperties.SQL_LOG_ENABLED)
@EnableConfigurationProperties({MybatisPluginProperties.class})
@RequiredArgsConstructor
@Slf4j
@Configuration(proxyBeanMethods = false)
public class MybatisPluginAutoConfiguration implements InitializingBean {

	public static final String AUTOWIRED_MYBATIS_SQL_LOG_INTERCEPTOR = "Autowired MybatisSqlLogInterceptor";

	private final List<SqlSessionFactory> sqlSessionFactories;

	@Override
	public void afterPropertiesSet() {
		log.debug(AUTOWIRED_MYBATIS_SQL_LOG_INTERCEPTOR);
		for (SqlSessionFactory sqlSessionFactory : sqlSessionFactories) {
			if (sqlSessionFactory != null) {
				sqlSessionFactory.getConfiguration().addInterceptor(new MybatisSqlLogInterceptor());
			}
		}
	}
}
