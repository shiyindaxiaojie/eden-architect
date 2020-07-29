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

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.MutablePropertyValues;
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
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.ylzl.eden.spring.boot.commons.lang.StringConstants;
import org.ylzl.eden.spring.boot.commons.lang.StringUtils;
import org.ylzl.eden.spring.boot.data.core.DataConstants;
import org.ylzl.eden.spring.boot.data.core.DataProperties;
import org.ylzl.eden.spring.boot.data.jdbc.datasource.routing.RoutingDataSourceProxy;
import org.ylzl.eden.spring.boot.data.liquibase.AsyncLiquibaseAutoConfiguration;
import org.ylzl.eden.spring.boot.data.mybatis.MybatisPageHelperAutoConfiguration;
import org.ylzl.eden.spring.boot.framework.core.bind.BinderHelper;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.Map;

/**
 * 动态路由数据源配置
 *
 * <p>从 Spring Boot 1.X 升级到 2.X
 *
 * <ul>
 *   <li>org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder 迁移到 {@link DataSourceBuilder}
 *   <li>org.springframework.boot.bind.RelaxedPropertyResolver
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
  AsyncLiquibaseAutoConfiguration.class,
  MybatisPageHelperAutoConfiguration.class
})
@EnableConfigurationProperties({
  DataSourceProperties.class,
  DataProperties.class,
  RoutingDataSourceProperties.class
})
@Slf4j
@Configuration
public class RoutingDataSourceAutoConfiguration
    implements ImportBeanDefinitionRegistrar, EnvironmentAware {

  private static final String PROP_ROUTING_DATA_SOURCE_PREFIX =
      DataConstants.PROP_PREFIX + ".routing-datasource";

  public static final String ROUTING_DATASOURCE_ENABLED = PROP_ROUTING_DATA_SOURCE_PREFIX + ".enabled";

  private static final String MSG_AUTOWIRED_ROUTING_DS = "Autowired routing Datasource";

  private static final Object DEFAULT_DATASOURCE_TYPE = "com.zaxxer.hikari.HikariDataSource";

  private static final String DEFAULT_BEAN_NAME = "dataSource";

  private static final String DEFAULT_DATASOURCE_NAME = "default";

  private Environment env;

  private BinderHelper binderHelper;

  @Override
  public void setEnvironment(Environment env) {
    this.env = env;
  	this.binderHelper = new BinderHelper(env);
  }

  @Override
  public void registerBeanDefinitions(
      AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
  	if (!env.containsProperty(ROUTING_DATASOURCE_ENABLED) ||
			!env.getProperty(ROUTING_DATASOURCE_ENABLED, Boolean.class)) {
  		return;
		}

    log.debug(MSG_AUTOWIRED_ROUTING_DS);
    GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
    beanDefinition.setBeanClass(RoutingDataSourceProxy.class);
    beanDefinition.setSynthetic(true);

    MutablePropertyValues mpv = beanDefinition.getPropertyValues();

    Map<String, DataSource> targetDataSources = getTargetDataSources();
    mpv.addPropertyValue("targetDataSources", targetDataSources);

    DataSource defaultTargetDataSource = getDefaultTargetDataSource();
    mpv.addPropertyValue("defaultTargetDataSource", defaultTargetDataSource);
    targetDataSources.put(DEFAULT_DATASOURCE_NAME, defaultTargetDataSource);

    registry.registerBeanDefinition(DEFAULT_BEAN_NAME, beanDefinition);
  }

  private DataSource getDefaultTargetDataSource() {
    DataSourceProperties dataSourceProperties =
        binderHelper.bind(
            StringUtils.join(DataConstants.PROP_DATASOURCE_PREFIX, StringConstants.DOT),
            DataSourceProperties.class);
    return this.buildDataSource(dataSourceProperties);
  }

  private Map<String, DataSource> getTargetDataSources() {
    RoutingDataSourceProperties properties =
        binderHelper.bind(
            StringUtils.join(PROP_ROUTING_DATA_SOURCE_PREFIX, StringConstants.DOT),
            RoutingDataSourceProperties.class);
    if (properties.getNodes() == null) {
      return Collections.EMPTY_MAP;
    }

    Map<String, DataSource> targetDataSources = Maps.newHashMap();
    for (DataSourceProperties dataSourceProperties : properties.getMetadata()) {
      DataSource dataSource = this.buildDataSource(dataSourceProperties);
      targetDataSources.put(dataSourceProperties.getName(), dataSource);
    }
    return targetDataSources;
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
      throw new RuntimeException(e.getMessage());
    }

    return DataSourceBuilder.create()
        .type(dataSourceType)
        .driverClassName(properties.getDriverClassName())
        .url(properties.getUrl())
        .username(properties.getUsername())
        .password(properties.getPassword())
        .build();
  }
}
