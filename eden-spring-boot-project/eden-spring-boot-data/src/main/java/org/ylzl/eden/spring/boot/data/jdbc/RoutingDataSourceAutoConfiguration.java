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
package org.ylzl.eden.spring.boot.data.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.ylzl.eden.spring.boot.commons.lang.StringConstants;
import org.ylzl.eden.spring.boot.commons.lang.StringUtils;
import org.ylzl.eden.spring.boot.data.core.DataConstants;
import org.ylzl.eden.spring.boot.data.core.DataProperties;
import org.ylzl.eden.spring.boot.data.jdbc.datasource.DataSourceEnum;
import org.ylzl.eden.spring.boot.data.jdbc.datasource.RoutingDataSourceProxy;
import org.ylzl.eden.spring.boot.data.liquibase.EnhancedLiquibaseAutoConfiguration;
import org.ylzl.eden.spring.boot.data.mybatis.MybatisPageHelperAutoConfiguration;
import org.ylzl.eden.spring.boot.framework.core.bind.BinderHelper;

import javax.sql.DataSource;
import java.util.*;

/**
 * 动态路由数据源配置
 *
 * <p>变更日志：Spring Boot 1.X 升级到 2.X</p>
 * <ul>
 *     <li>org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder 迁移到 {@link DataSourceBuilder}</li>
 *     <li>org.springframework.boot.bind.RelaxedPropertyResolver</li>
 * </ul>
 *
 * @author gyl
 * @since 1.0.0
 */
@AutoConfigureBefore({
	DataSourceAutoConfiguration.class,
	DataSourceTransactionManagerAutoConfiguration.class,
	HibernateJpaAutoConfiguration.class,
	EnhancedJdbcTemplateAutoConfiguration.class,
	EnhancedLiquibaseAutoConfiguration.class,
	MybatisPageHelperAutoConfiguration.class
})
@ConditionalOnExpression(RoutingDataSourceAutoConfiguration.EXPS_ROUTING_DATASOURCE_ENABLED)
@EnableConfigurationProperties({DataSourceProperties.class, DataProperties.class, RoutingDataSourceProperties.class})
@Slf4j
@Configuration
public class RoutingDataSourceAutoConfiguration {

	public static final String EXPS_ROUTING_DATASOURCE_ENABLED = "${" + DataConstants.PROP_PREFIX + ".jdbc.routing-datasource.enabled:false}";

	@Configuration
	public static class InternalRoutingDataSourceAutoConfiguration implements ImportBeanDefinitionRegistrar, EnvironmentAware {

		private static final Object DEFAULT_DATASOURCE_TYPE = "com.zaxxer.hikari.HikariDataSource";

		private static final String DEFAULT_BEAN_NAME = "dataSource";

		private static final String PROP_ROUTING_DATA_SOURCE_PREFIX = DataConstants.PROP_PREFIX + ".jdbc.routing-data-source";

		private static final String PROP_DATASOURCE_TYPE = "type";

		private static final String PROP_DATASOURCE_DRIVEE_CLASS_NAME = "driver-class-name";

		private static final String PROP_DATASOURCE_URL = "url";

		private static final String PROP_DATASOURCE_USERNAME = "username";

		private static final String PROP_DATASOURCE_PASSWORD = "password";

		private static final String MSG_INJECT_ROUTING_DS = "Inject routing Datasource";

		private static final String MSG_INVALID_TYPE_EXCEPTION = "Invalid Datasource";

		private DataSource defaultTargetDataSource;

		private Map<String, DataSource> targetDataSources = new HashMap<>();

		private List<String> masterDataSources = new ArrayList<>();

		private List<String> slaveDataSources = new ArrayList<>();

		private PropertyValues propertyValues;

		private BinderHelper binderHelper;

		@Override
		public void setEnvironment(Environment env) {
			binderHelper = new BinderHelper(env);
			this.setDefaultTargetDataSource();
			this.setTargetDataSources();
		}

		@Override
		public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
			log.debug(MSG_INJECT_ROUTING_DS);
			GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
			beanDefinition.setBeanClass(RoutingDataSourceProxy.class);
			beanDefinition.setSynthetic(true);
			MutablePropertyValues mpv = beanDefinition.getPropertyValues();
			mpv.addPropertyValue("defaultTargetDataSource", defaultTargetDataSource);
			mpv.addPropertyValue("targetDataSources", targetDataSources);
			mpv.addPropertyValue("masterDataSources", masterDataSources);
			mpv.addPropertyValue("slaveDataSources", slaveDataSources);
			registry.registerBeanDefinition(DEFAULT_BEAN_NAME, beanDefinition);
		}

		private void setDefaultTargetDataSource() {
			DataSourceProperties dataSourceProperties = binderHelper.bind(StringUtils.join(DataConstants.PROP_DATASOURCE_PREFIX, StringConstants.DOT), DataSourceProperties.class);
			DataSource dataSource = this.buildDataSource(dataSourceProperties);
			defaultTargetDataSource = dataSource;
			String key = StringConstants.COMMA;
			masterDataSources.add(key);
			targetDataSources.put(key, dataSource);
		}

		private void setTargetDataSources() {
			RoutingDataSourceProperties properties = binderHelper.bind(StringUtils.join(PROP_ROUTING_DATA_SOURCE_PREFIX, StringConstants.DOT), RoutingDataSourceProperties.class);
			if (properties.getNodes() == null) {
				return;
			}

			for (RoutingDataSourceProperties.EnhancedDataSourceProperties dataSourceProperties : properties.getDataSources()) {
				DataSource dataSource = this.buildDataSource(dataSourceProperties);
				if (DataSourceEnum.SLAVE.name().equalsIgnoreCase(dataSourceProperties.getEnumType())) {
					slaveDataSources.add(dataSourceProperties.getName());
				} else if (DataSourceEnum.MASTER.name().equalsIgnoreCase(dataSourceProperties.getEnumType())) {
					masterDataSources.add(dataSourceProperties.getName());
				}
				targetDataSources.put(dataSourceProperties.getName(), dataSource);
			}
		}

		@SuppressWarnings("unchecked")
		private DataSource buildDataSource(DataSourceProperties properties) {
			Object type = properties.getType();
			if (type == null) {
				type = DEFAULT_DATASOURCE_TYPE;
			}
			Class<? extends DataSource> dataSourceType;
			try {
				dataSourceType = (Class<? extends DataSource>) Class.forName((String) type);
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(MSG_INVALID_TYPE_EXCEPTION);
			}

			return DataSourceBuilder.create()
				.type(dataSourceType)
				.driverClassName(properties.getDriverClassName())
				.url(properties.getUrl())
				.username(properties.getUsername())
				.password(properties.getPassword()).build();
		}
	}
}
