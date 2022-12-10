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

package org.ylzl.eden.liquibase.spring.boot.autoconfigure;

import liquibase.change.DatabaseChange;
import liquibase.integration.spring.SpringLiquibase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseDataSource;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.core.task.AsyncTaskExecutor;
import org.ylzl.eden.liquibase.spring.boot.env.ExtendLiquibaseProperties;
import org.ylzl.eden.spring.boot.bootstrap.constant.Conditions;
import org.ylzl.eden.spring.data.liquibase.util.SpringLiquibaseUtils;

import javax.sql.DataSource;
import java.util.concurrent.Executor;

/**
 * Liquibase 自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */

@ConditionalOnProperty(
	prefix = "spring.liquibase",
	name = Conditions.ENABLED,
	havingValue = Conditions.TRUE,
	matchIfMissing = true
)
@ConditionalOnBean({DataSource.class, Executor.class})
@ConditionalOnClass({SpringLiquibase.class, DatabaseChange.class})
@AutoConfigureAfter({
	DataSourceAutoConfiguration.class,
	HibernateJpaAutoConfiguration.class
})
@AutoConfigureBefore({LiquibaseAutoConfiguration.class})
@EnableConfigurationProperties({LiquibaseProperties.class, ExtendLiquibaseProperties.class})
@RequiredArgsConstructor
@Slf4j
@Configuration(proxyBeanMethods = false)
public class AsyncLiquibaseAutoConfiguration {

	private static final String MSG_AUTOWIRED_LIQUIBASE = "Autowired SpringLiquibase";

	private final LiquibaseProperties properties;

	private final ExtendLiquibaseProperties extendProperties;

	@ConditionalOnMissingBean
	@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
	@Bean
	public SpringLiquibase liquibase(ObjectProvider<DataSource> dataSource, DataSourceProperties dataSourceProperties,
		@Qualifier(TaskExecutionAutoConfiguration.APPLICATION_TASK_EXECUTOR_BEAN_NAME) AsyncTaskExecutor taskExecutor,
		@LiquibaseDataSource ObjectProvider<DataSource> liquibaseDataSource) {
		log.debug(MSG_AUTOWIRED_LIQUIBASE);
		SpringLiquibase liquibase = SpringLiquibaseUtils.createAsyncSpringLiquibase(
				extendProperties.isAsync(),
				taskExecutor,
				liquibaseDataSource.getIfAvailable(),
				properties,
				dataSource.getIfUnique(),
				dataSourceProperties);
		liquibase.setContexts(properties.getContexts());
		liquibase.setDefaultSchema(properties.getDefaultSchema());
		liquibase.setLiquibaseSchema(properties.getLiquibaseSchema());
		liquibase.setLiquibaseTablespace(properties.getLiquibaseTablespace());
		liquibase.setDatabaseChangeLogLockTable(properties.getDatabaseChangeLogLockTable());
		liquibase.setDatabaseChangeLogTable(properties.getDatabaseChangeLogTable());
		liquibase.setDropFirst(properties.isDropFirst());
		liquibase.setLabels(properties.getLabels());
		liquibase.setChangeLogParameters(properties.getParameters());
		liquibase.setRollbackFile(properties.getRollbackFile());
		liquibase.setTestRollbackOnUpdate(properties.isTestRollbackOnUpdate());
		liquibase.setChangeLog(properties.getChangeLog());
		return liquibase;
	}
}
