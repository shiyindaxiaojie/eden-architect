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
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.bind.RelaxedDataBinder;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
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

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 动态路由数据源配置
 *
 * @author gyl
 * @since 0.0.1
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
@EnableConfigurationProperties({DataSourceProperties.class, DataProperties.class})
@Slf4j
@Configuration
public class RoutingDataSourceAutoConfiguration {

  public static final String EXPS_ROUTING_DATASOURCE_ENABLED =
      "${" + DataConstants.PROP_PREFIX + ".routing-datasource.enabled:false}";

  @Configuration
  public static class InternalRoutingDataSourceAutoConfiguration
      implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    private static final Object DEFAULT_DATASOURCE_TYPE = "com.zaxxer.hikari.HikariDataSource";

    private static final String DEFAULT_BEAN_NAME = "dataSource";

    private static final String PROP_SPRING_DATA_PROPERTIES_DS_PREFIX =
        DataConstants.PROP_PREFIX + ".data-source";

    private static final String PROP_DATASOURCE_NODES = "nodes";

    private static final String PROP_DATASOURCE_TYPE = "type";

    private static final String PROP_DATASOURCE_DRIVEE_CLASS_NAME = "driver-class-name";

    private static final String PROP_DATASOURCE_URL = "url";

    private static final String PROP_DATASOURCE_USERNAME = "username";

    private static final String PROP_DATASOURCE_PASSWORD = "password";

    private static final String PROP_DATASOURCE_ENUM = "enum";

    private static final String MSG_INJECT_ROUTING_DS = "Inject routing Datasource";

    private static final String MSG_INVALID_TYPE_EXCEPTION = "Invalid Datasource";

    private ConversionService conversionService = new DefaultConversionService();

    private DataSource defaultTargetDataSource;

    private Map<String, DataSource> targetDataSources = new HashMap<>();

    private List<String> masterDataSources = new ArrayList<>();

    private List<String> slaveDataSources = new ArrayList<>();

    private PropertyValues propertyValues;

    @Override
    public void setEnvironment(Environment env) {
      this.setDefaultTargetDataSource(env);
      this.setTargetDataSources(env);
    }

    @Override
    public void registerBeanDefinitions(
        AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
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

    private void setDefaultTargetDataSource(Environment env) {
      RelaxedPropertyResolver resolver =
          new RelaxedPropertyResolver(
              env, StringUtils.join(DataConstants.PROP_PREFIX, StringConstants.DOT));
      Map<String, Object> properties = new HashMap<>();
      properties.put(PROP_DATASOURCE_TYPE, resolver.getProperty(PROP_DATASOURCE_TYPE));
      properties.put(
          PROP_DATASOURCE_DRIVEE_CLASS_NAME,
          resolver.getProperty(PROP_DATASOURCE_DRIVEE_CLASS_NAME));
      properties.put(PROP_DATASOURCE_URL, resolver.getProperty(PROP_DATASOURCE_URL));
      properties.put(PROP_DATASOURCE_USERNAME, resolver.getProperty(PROP_DATASOURCE_USERNAME));
      properties.put(PROP_DATASOURCE_PASSWORD, resolver.getProperty(PROP_DATASOURCE_PASSWORD));
      DataSource dataSource = this.buildDataSource(properties);
      dataBinder(dataSource, env);
      defaultTargetDataSource = dataSource;
      String key = StringConstants.COMMA;
      masterDataSources.add(key);
      targetDataSources.put(key, dataSource);
    }

    private void setTargetDataSources(Environment env) {
      RelaxedPropertyResolver resolver =
          new RelaxedPropertyResolver(
              env, StringUtils.join(PROP_SPRING_DATA_PROPERTIES_DS_PREFIX, StringConstants.DOT));
      String nodes = resolver.getProperty(PROP_DATASOURCE_NODES);
      if (nodes == null) {
        return;
      }
      String[] keys = nodes.split(StringConstants.COMMA);
      for (String key : keys) {
        if (StringUtils.isBlank(key)) {
          continue;
        }
        Map<String, Object> properties =
            resolver.getSubProperties(StringUtils.join(key, StringConstants.DOT));
        String enumName =
            ObjectUtils.toString(properties.get(PROP_DATASOURCE_ENUM), DataSourceEnum.SLAVE.name());
        if (DataSourceEnum.SLAVE.name().equalsIgnoreCase(enumName)) {
          slaveDataSources.add(key);
        } else if (DataSourceEnum.MASTER.name().equalsIgnoreCase(enumName)) {
          masterDataSources.add(key);
        }
        DataSource dataSource = this.buildDataSource(properties);
        dataBinder(dataSource, env);
        targetDataSources.put(key, dataSource);
      }
    }

    @SuppressWarnings("unchecked")
    private DataSource buildDataSource(Map<String, Object> properties) {
      Object type = properties.get(PROP_DATASOURCE_TYPE);
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
          .driverClassName(ObjectUtils.toString(properties.get(PROP_DATASOURCE_DRIVEE_CLASS_NAME)))
          .url(ObjectUtils.toString(properties.get(PROP_DATASOURCE_URL)))
          .username(ObjectUtils.toString(properties.get(PROP_DATASOURCE_USERNAME)))
          .password(ObjectUtils.toString(properties.get(PROP_DATASOURCE_PASSWORD)))
          .build();
    }

    private void dataBinder(DataSource dataSource, Environment env) {
      RelaxedDataBinder dataBinder = new RelaxedDataBinder(dataSource);
      dataBinder.setConversionService(conversionService);
      dataBinder.setIgnoreNestedProperties(false);
      dataBinder.setIgnoreInvalidFields(false);
      dataBinder.setIgnoreUnknownFields(true);
      if (propertyValues == null) {
        Map<String, Object> subProperties =
            new RelaxedPropertyResolver(env, DataConstants.PROP_PREFIX)
                .getSubProperties(StringConstants.DOT);
        HashMap<String, Object> properties = new HashMap<>();
        properties.putAll(subProperties);
        // 排除已经设置的属性
        properties.remove(PROP_DATASOURCE_TYPE);
        properties.remove(PROP_DATASOURCE_DRIVEE_CLASS_NAME);
        properties.remove(PROP_DATASOURCE_URL);
        properties.remove(PROP_DATASOURCE_USERNAME);
        properties.remove(PROP_DATASOURCE_PASSWORD);
        propertyValues = new MutablePropertyValues(properties);
      }
      dataBinder.bind(propertyValues);
    }
  }
}
