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

package org.ylzl.eden.druid.spring.boot.autoconfigure;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.jdbc.DataSourceUnwrapper;
import org.springframework.boot.jdbc.metadata.DataSourcePoolMetadataProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.ylzl.eden.druid.spring.boot.jdbc.DruidDataSourcePoolMetadata;

/**
 *  Druid 数据源监控连接池
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@ConditionalOnProperty(DruidDataSourceMetricsAutoConfiguration.SPRING_DATASOURCE_DRUID)
@ConditionalOnClass(DruidDataSource.class)
@AutoConfigureAfter(DruidDataSourceAutoConfigure.class)
@Slf4j
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Configuration(proxyBeanMethods = false)
public class DruidDataSourceMetricsAutoConfiguration {

	public static final String SPRING_DATASOURCE_DRUID = "spring.datasource.druid";

	private static final String AUTOWIRED_DATA_SOURCE_POOL_METADATA_PROVIDER = "Autowired DataSourcePoolMetadataProvider";

	@ConditionalOnMissingBean
	@ConditionalOnBean({MeterRegistry.class, DruidDataSource.class})
	@Bean
	public DataSourcePoolMetadataProvider druidPoolDataSourceMetadataProvider() {
		log.debug(AUTOWIRED_DATA_SOURCE_POOL_METADATA_PROVIDER);
		return (dataSource) -> {
			DruidDataSource ds = DataSourceUnwrapper.unwrap(dataSource, DruidDataSource.class);
			if (ds != null) {
				return new DruidDataSourcePoolMetadata(ds);
			}
			return null;
		};
	}
}
