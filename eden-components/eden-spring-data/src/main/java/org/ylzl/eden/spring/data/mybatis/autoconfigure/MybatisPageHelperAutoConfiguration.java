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

package org.ylzl.eden.spring.data.mybatis.autoconfigure;

import com.github.pagehelper.PageInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.Properties;

/**
 * Mybatis 分页插件自动配置
 *
 * @author gyl
 * @since 2.4.x
 */
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@AutoConfigureBefore({CustomMybatisAutoConfiguration.class})
@EnableConfigurationProperties({MybatisProperties.class})
@ConditionalOnClass({PageInterceptor.class})
@Import(CustomMybatisAutoConfiguration.class)
@Slf4j
@Configuration
public class MybatisPageHelperAutoConfiguration {

	private static final String MSG_AUTOWIRED_MYBATIS_PAGE_INTERCEPTOR =
		"Autowired Mybatis PageHelper";

	@ConditionalOnMissingBean
	@Bean
	public PageInterceptor pageInterceptor() {
		log.debug(MSG_AUTOWIRED_MYBATIS_PAGE_INTERCEPTOR);
		PageInterceptor pageInterceptor = new PageInterceptor();
		Properties properties = new Properties();
		properties.setProperty("supportMethodsArguments", "true");
		properties.setProperty("params", "count=countSql");
		pageInterceptor.setProperties(properties);
		return pageInterceptor;
	}
}
