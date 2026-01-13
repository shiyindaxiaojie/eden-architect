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

package org.ylzl.eden.shardingsphere.spring.boot.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.driver.jdbc.core.datasource.ShardingSphereDataSource;
import org.apache.shardingsphere.infra.metadata.database.ShardingSphereDatabase;
import org.apache.shardingsphere.mode.manager.ContextManager;
import org.ylzl.eden.spring.data.jdbc.datasource.DataSourceUrlParser;
import org.ylzl.eden.spring.data.jdbc.datasource.DataSourceUrlParserException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.Map;

/**
 * ShardingSphere 5.4.x 数据源 URL 解析器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Slf4j
public class ShardingSphereDataSourceUrlParser implements DataSourceUrlParser {

	/**
	 * 获取数据源地址
	 *
	 * @param dataSource 数据源
	 * @return 数据源地址
	 */
	@Override
	public String getDataSourceUrl(DataSource dataSource) {
		if (!ShardingSphereDataSource.class.isAssignableFrom(dataSource.getClass())) {
			return null;
		}

		try {
			ShardingSphereDataSource shardingSphereDataSource = (ShardingSphereDataSource) dataSource;
			return extractUrlFromShardingSphereDataSource(shardingSphereDataSource);
		} catch (Exception ex) {
			log.error("Failed to extract URL from ShardingSphereDataSource: {}", ex.getMessage(), ex);
			throw new DataSourceUrlParserException(ex.getMessage(), ex);
		}
	}

	/**
	 * 从 ShardingSphereDataSource 中提取 URL
	 * 适配 ShardingSphere 5.4.x API
	 */
	private String extractUrlFromShardingSphereDataSource(ShardingSphereDataSource dataSource) throws Exception {
		// 方式1: 尝试通过 ContextManager 获取
		try {
			ContextManager contextManager = dataSource.getContextManager();
			Map<String, ShardingSphereDatabase> databases = contextManager
				.getMetaDataContexts()
				.getMetaData()
				.getDatabases();

			// 获取第一个数据库的第一个数据源 URL
			for (ShardingSphereDatabase database : databases.values()) {
				Map<String, DataSource> dataSources = database.getResourceMetaData().getStorageUnits()
					.entrySet().stream()
					.collect(java.util.stream.Collectors.toMap(
						Map.Entry::getKey,
						e -> e.getValue().getDataSource()
					));

				for (DataSource ds : dataSources.values()) {
					String url = extractUrlFromDataSource(ds);
					if (url != null) {
						return url;
					}
				}
			}
		} catch (Exception e) {
			log.debug("Failed to extract URL via ContextManager, trying connection method: {}", e.getMessage());
		}

		// 方式2: 通过获取连接的方式获取 URL
		try (Connection connection = dataSource.getConnection()) {
			DatabaseMetaData metaData = connection.getMetaData();
			return metaData.getURL();
		}
	}

	/**
	 * 从普通 DataSource 中提取 URL
	 */
	private String extractUrlFromDataSource(DataSource dataSource) {
		try (Connection connection = dataSource.getConnection()) {
			DatabaseMetaData metaData = connection.getMetaData();
			return metaData.getURL();
		} catch (Exception e) {
			log.debug("Failed to extract URL from DataSource: {}", e.getMessage());
			return null;
		}
	}
}
