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

package org.ylzl.eden.spring.data.jdbc.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.ylzl.eden.spring.data.jdbc.namedparam.BatchNamedParameterJdbcTemplate;

import javax.sql.DataSource;

/**
 * Jdbc 模板自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@ConditionalOnClass(NamedParameterJdbcTemplate.class)
@AutoConfigureBefore({JdbcTemplateAutoConfiguration.class})
@Slf4j
@Configuration
public class BatchJdbcTemplateAutoConfiguration {

	public static final String MSG_AUTOWIRED_BATCH_JDBCTPL =
		"Autowired BatchNamedParameterJdbcTemplate";

	/**
	 * Inject EnhancedNamedParameterJdbcTemplate
	 *
	 * @param dataSource 数据源
	 * @return EnhancedNamedParameterJdbcTemplate 实例
	 */
	@ConditionalOnMissingBean
	@Bean
	public BatchNamedParameterJdbcTemplate batchNamedParameterJdbcTemplate(DataSource dataSource) {
		log.debug(MSG_AUTOWIRED_BATCH_JDBCTPL);
		return new BatchNamedParameterJdbcTemplate(dataSource);
	}
}
