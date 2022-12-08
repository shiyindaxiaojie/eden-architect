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

package org.ylzl.eden.mongobee.spring.boot.autoconfigure;

import com.github.mongobee.Mongobee;
import com.mongodb.MongoClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.ylzl.eden.commons.lang.StringUtils;
import org.ylzl.eden.mongobee.spring.boot.env.MongobeeProperties;
import org.ylzl.eden.spring.boot.bootstrap.constant.Conditions;
import org.ylzl.eden.spring.boot.info.contributor.InfoContributorProvider;
import org.ylzl.eden.spring.data.mongobee.async.AsyncMongobee;

/**
 * MongoDB 配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ConditionalOnProperty(
	prefix = MongobeeProperties.PREFIX,
	name = Conditions.ENABLED,
	havingValue = Conditions.TRUE,
	matchIfMissing = true
)
@ConditionalOnClass({Mongobee.class})
@EnableConfigurationProperties(MongobeeProperties.class)
@Import(MongoAutoConfiguration.class)
@RequiredArgsConstructor
@Slf4j
@Configuration(proxyBeanMethods = false)
public class MongobeeAutoConfiguration {

	private static final String DEFAULT_CONFIG_DBMIGRATIONS_SUFFIX = "config.dbmigrations";

	private static final String MSG_AUTOWIRED_MONGOBEE = "Autowired Mongobee";

	private final MongobeeProperties mongobeeProperties;

	private final MongoProperties mongoProperties;

	private final MongoClient mongoClient;

	private final MongoTemplate mongoTemplate;

	@Bean
	public LocalValidatorFactoryBean localValidator() {
		return new LocalValidatorFactoryBean();
	}

	@Bean
	public ValidatingMongoEventListener validatingMongoEventListener() {
		return new ValidatingMongoEventListener(localValidator());
	}

	@Bean
	public Mongobee mongobee(InfoContributorProvider infoContributorProvider,
		@Qualifier(TaskExecutionAutoConfiguration.APPLICATION_TASK_EXECUTOR_BEAN_NAME) TaskExecutor taskExecutor) {
		log.debug(MSG_AUTOWIRED_MONGOBEE);
		Mongobee mongobee = new AsyncMongobee(mongobeeProperties.isAsync(), taskExecutor, mongoClient);
		mongobee.setDbName(mongoProperties.getMongoClientDatabase());
		mongobee.setMongoTemplate(mongoTemplate);
		if (StringUtils.isNotBlank(mongobeeProperties.getChangeLogsScanPackage())) {
			mongobee.setChangeLogsScanPackage(mongobeeProperties.getChangeLogsScanPackage());
		} else {
			mongobee.setChangeLogsScanPackage(
				infoContributorProvider.resolvePackage(DEFAULT_CONFIG_DBMIGRATIONS_SUFFIX));
		}
		mongobee.setEnabled(mongobeeProperties.isEnabled());
		return mongobee;
	}
}
