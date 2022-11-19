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

package org.ylzl.eden.mybatis.spring.boot.autoconfigure;

import com.github.pagehelper.PageInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * Mybatis 分页插件自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@ConditionalOnClass({PageInterceptor.class})
@Slf4j
@Configuration(proxyBeanMethods = false)
public class MybatisPageHelperAutoConfiguration {

	public static final String AUTOWIRED_PAGE_HELPER = "Autowired PageHelper";

	@ConditionalOnMissingBean
	@Bean
	public PageInterceptor pageInterceptor() {
		log.debug(AUTOWIRED_PAGE_HELPER);
		PageInterceptor pageInterceptor = new PageInterceptor();
		Properties properties = new Properties();
		// Fixed：关闭特殊字段导致自动分页
		properties.setProperty("supportMethodsArguments", "false");
		properties.setProperty("params", "count=countSql");
		pageInterceptor.setProperties(properties);
		return pageInterceptor;
	}
}
