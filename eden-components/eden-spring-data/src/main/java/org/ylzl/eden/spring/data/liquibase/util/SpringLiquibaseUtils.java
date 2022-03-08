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

package org.ylzl.eden.spring.data.liquibase.util;

import liquibase.integration.spring.SpringLiquibase;
import lombok.experimental.UtilityClass;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.liquibase.DataSourceClosingSpringLiquibase;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.core.env.Environment;
import org.ylzl.eden.spring.data.liquibase.async.AsyncSpringLiquibase;

import javax.sql.DataSource;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

/**
 * SpringLiquibase 工具集
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.0.0
 */
@UtilityClass
public class SpringLiquibaseUtils {

	public static SpringLiquibase createSpringLiquibase(
		DataSource liquibaseDatasource,
		LiquibaseProperties liquibaseProperties,
		DataSource dataSource,
		DataSourceProperties dataSourceProperties) {
		SpringLiquibase liquibase;
		DataSource liquibaseDataSource =
			getDataSource(liquibaseDatasource, liquibaseProperties, dataSource);
		if (liquibaseDataSource != null) {
			liquibase = new SpringLiquibase();
			liquibase.setDataSource(liquibaseDataSource);
			return liquibase;
		}
		liquibase = new DataSourceClosingSpringLiquibase();
		liquibase.setDataSource(createNewDataSource(liquibaseProperties, dataSourceProperties));
		return liquibase;
	}

	public static SpringLiquibase createAsyncSpringLiquibase(
		Environment env,
		Executor executor,
		DataSource liquibaseDatasource,
		LiquibaseProperties liquibaseProperties,
		DataSource dataSource,
		DataSourceProperties dataSourceProperties) {
		SpringLiquibase liquibase;
		DataSource liquibaseDataSource =
			getDataSource(liquibaseDatasource, liquibaseProperties, dataSource);
		if (liquibaseDataSource != null) {
			liquibase = new AsyncSpringLiquibase(executor, env);
			((AsyncSpringLiquibase) liquibase).setCloseDataSourceOnceMigrated(false);
			liquibase.setDataSource(liquibaseDataSource);
			return liquibase;
		}
		liquibase = new AsyncSpringLiquibase(executor, env);
		liquibase.setDataSource(createNewDataSource(liquibaseProperties, dataSourceProperties));
		return liquibase;
	}

	private static DataSource getDataSource(
		DataSource liquibaseDataSource,
		LiquibaseProperties liquibaseProperties,
		DataSource dataSource) {
		if (liquibaseDataSource != null) {
			return liquibaseDataSource;
		}
		if (liquibaseProperties.getUrl() == null && liquibaseProperties.getUser() == null) {
			return dataSource;
		}
		return null;
	}

	private static DataSource createNewDataSource(
		LiquibaseProperties liquibaseProperties, DataSourceProperties dataSourceProperties) {
		String url = getProperty(liquibaseProperties::getUrl, dataSourceProperties::determineUrl);
		String user =
			getProperty(liquibaseProperties::getUser, dataSourceProperties::determineUsername);
		String password =
			getProperty(liquibaseProperties::getPassword, dataSourceProperties::determinePassword);
		return DataSourceBuilder.create().url(url).username(user).password(password).build();
	}

	private static String getProperty(Supplier<String> property, Supplier<String> defaultValue) {
		String value = property.get();
		return (value != null) ? value : defaultValue.get();
	}
}
