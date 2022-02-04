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

package org.ylzl.eden.spring.data.liquibase.autoconfigure;

import liquibase.change.DatabaseChange;
import liquibase.integration.spring.SpringLiquibase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.core.env.Environment;
import org.ylzl.eden.spring.data.liquibase.util.SpringLiquibaseUtils;
import org.ylzl.eden.spring.framework.task.autoconfigure.AsyncTaskExecutionAutoConfiguration;

import javax.sql.DataSource;
import java.util.concurrent.Executor;

/**
 * Liquibase 自动装配
 *
 * @author gyl
 * @since 2.4.x
 */
@AutoConfigureAfter({
	DataSourceAutoConfiguration.class,
	HibernateJpaAutoConfiguration.class,
	AsyncTaskExecutionAutoConfiguration.class
})
@AutoConfigureBefore({LiquibaseAutoConfiguration.class})
@ConditionalOnBean({DataSource.class})
@ConditionalOnClass({SpringLiquibase.class, DatabaseChange.class})
@ConditionalOnProperty(prefix = "spring.liquibase", name = "enabled", matchIfMissing = true)
@EnableConfigurationProperties({LiquibaseProperties.class})
@Slf4j
@Configuration(proxyBeanMethods = false)
public class AsyncLiquibaseAutoConfiguration {

	private static final String DEFAULT_CHANGE_LOG = "classpath*:db/master.xml";

	private static final String MSG_AUTOWIRED_LIQUIBASE = "Autowired SpringLiquibase";

	private final LiquibaseProperties properties;

	private final Environment env;

	public AsyncLiquibaseAutoConfiguration(LiquibaseProperties properties, Environment env) {
		this.properties = properties;
		this.env = env;
	}

	@ConditionalOnMissingBean
	@Bean
	public SpringLiquibase liquibase(
		@Qualifier(TaskExecutionAutoConfiguration.APPLICATION_TASK_EXECUTOR_BEAN_NAME) Executor taskExecutor,
		@LiquibaseDataSource ObjectProvider<DataSource> liquibaseDataSource,
		ObjectProvider<DataSource> dataSource,
		DataSourceProperties dataSourceProperties,
		@Value("${spring.liquibase.change-log:" + DEFAULT_CHANGE_LOG + "}") String changeLog) {
		log.debug(MSG_AUTOWIRED_LIQUIBASE);
		SpringLiquibase liquibase =
			SpringLiquibaseUtils.createAsyncSpringLiquibase(
				this.env,
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
		liquibase.setChangeLog(changeLog);
		return liquibase;
	}
}
