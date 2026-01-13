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

package org.ylzl.eden.shardingsphere.spring.boot.autoconfigure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.driver.ShardingSphereDriver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.ylzl.eden.shardingsphere.spring.boot.env.ShardingSphereProperties;
import org.ylzl.eden.shardingsphere.spring.boot.exception.ShardingSphereConfigException;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * ShardingSphere 自动配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Slf4j
@RequiredArgsConstructor
@EnableConfigurationProperties(ShardingSphereProperties.class)
@ConditionalOnClass(ShardingSphereDriver.class)
@ConditionalOnProperty(prefix = ShardingSphereProperties.PREFIX, name = "enabled", havingValue = "true")
@Configuration
public class ShardingSphereAutoConfiguration {

	private static final String JDBC_URL_PREFIX = "jdbc:shardingsphere:";
	private static final String CLASSPATH_PREFIX = "classpath:";
	private static final String FILE_PREFIX = "file:";
	private static final String ABSOLUTE_PATH_PREFIX = "absolutepath:";

	private final ShardingSphereProperties properties;

	@Bean
	@Primary
	@ConditionalOnMissingBean
	public DataSource shardingSphereDataSource() throws SQLException {
		String configFile = properties.getConfigFile();
		log.info("Initializing ShardingSphere DataSource with config file: {}", configFile);

		// 验证配置文件是否存在
		validateConfigFile(configFile);

		// 构建 JDBC URL
		String jdbcUrl = buildJdbcUrl(configFile);
		log.debug("ShardingSphere JDBC URL: {}", jdbcUrl);

		// 使用 ShardingSphere Driver 创建 DataSource
		ShardingSphereDriver driver = new ShardingSphereDriver();
		return driver.connect(jdbcUrl, null).unwrap(DataSource.class);
	}

	/**
	 * 验证配置文件是否存在
	 */
	private void validateConfigFile(String configFile) {
		ResourceLoader resourceLoader = new DefaultResourceLoader();
		Resource resource;

		if (configFile.startsWith(CLASSPATH_PREFIX)) {
			resource = resourceLoader.getResource(configFile);
		} else if (configFile.startsWith(FILE_PREFIX)) {
			resource = resourceLoader.getResource(configFile);
		} else {
			// 默认作为 classpath 资源处理
			resource = resourceLoader.getResource(CLASSPATH_PREFIX + configFile);
		}

		if (!resource.exists()) {
			throw new ShardingSphereConfigException(
				"ShardingSphere config file not found: " + configFile, configFile);
		}
	}

	/**
	 * 构建 ShardingSphere JDBC URL
	 *
	 * @param configFile 配置文件路径
	 * @return JDBC URL
	 */
	String buildJdbcUrl(String configFile) {
		if (configFile.startsWith(CLASSPATH_PREFIX)) {
			return JDBC_URL_PREFIX + configFile;
		} else if (configFile.startsWith(FILE_PREFIX)) {
			// file:/path/to/config.yaml -> jdbc:shardingsphere:absolutepath:/path/to/config.yaml
			String absolutePath = configFile.substring(FILE_PREFIX.length());
			return JDBC_URL_PREFIX + ABSOLUTE_PATH_PREFIX + absolutePath;
		}
		// 默认作为 classpath 资源处理
		return JDBC_URL_PREFIX + CLASSPATH_PREFIX + configFile;
	}
}
