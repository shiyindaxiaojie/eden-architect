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
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.bind.RelaxedDataBinder;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.ylzl.eden.spring.boot.commons.lang.ObjectUtils;
import org.ylzl.eden.spring.boot.commons.lang.StringConstants;
import org.ylzl.eden.spring.boot.commons.lang.StringUtils;
import org.ylzl.eden.spring.boot.data.core.DataConstants;
import org.ylzl.eden.spring.boot.data.core.DataProperties;
import org.ylzl.eden.spring.boot.data.jdbc.datasource.routing.DynamicRoutingDataSource;
import org.ylzl.eden.spring.boot.data.jdbc.datasource.routing.RoutingDataSourceAspect;
import org.ylzl.eden.spring.boot.data.jdbc.datasource.routing.RoutingDataSourceDefault;
import org.ylzl.eden.spring.boot.data.liquibase.EnhancedLiquibaseAutoConfiguration;
import org.ylzl.eden.spring.boot.data.mybatis.MybatisPageHelperAutoConfiguration;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 动态路由数据源配置
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
@EnableConfigurationProperties({DataSourceProperties.class, DataProperties.class})
@Slf4j
@Configuration
public class RoutingDataSourceAutoConfiguration {

	public static final String EXPS_ROUTING_DATASOURCE_ENABLED =
		"${" + RoutingDataSourceDefault.PROP_SPRING_DATA_PROPS_DS_PREFIX + ".enabled:false}";

	@ConditionalOnMissingBean
	@Bean
	public RoutingDataSourceAspect routingDataSourceAspect() {
		return new RoutingDataSourceAspect();
	}

	@Configuration
	public static class InternalRoutingDataSourceAutoConfiguration
		implements ImportBeanDefinitionRegistrar, EnvironmentAware {

		private static final String MSG_INJECT_ROUTING_DS = "Autowired routing Datasource";

		private static final String DEFAULT_KEY = "default";

		private static final String SETTER_TARGET_DS = "targetDataSources";

		private static final String SETTER_DEFAULT_TARGET_DS = "defaultTargetDataSource";

		private ConversionService conversionService = new DefaultConversionService();

		private PropertyValues propertyValues;

		private Environment env;

		@Override
		public void setEnvironment(Environment env) {
			this.env = env;
		}

		@Override
		public void registerBeanDefinitions(
			AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
			log.debug(MSG_INJECT_ROUTING_DS);
			GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
			beanDefinition.setBeanClass(DynamicRoutingDataSource.class);
			beanDefinition.setSynthetic(true);
			MutablePropertyValues mpv = beanDefinition.getPropertyValues();

			Map<String, DataSource> targetDataSources = this.getTargetDataSources();
			mpv.addPropertyValue(SETTER_TARGET_DS, targetDataSources);

			DataSource defaultTargetDataSource = this.getDefaultTargetDataSource();
			mpv.addPropertyValue(SETTER_DEFAULT_TARGET_DS, defaultTargetDataSource);
			targetDataSources.put(DEFAULT_KEY, defaultTargetDataSource);

			registry.registerBeanDefinition(RoutingDataSourceDefault.BEAN_NAME, beanDefinition);
		}

		private DataSource getDefaultTargetDataSource() {
			RelaxedPropertyResolver resolver = new RelaxedPropertyResolver(env);
			String keyPrefix =
				StringUtils.join(DataConstants.PROP_DATASOURCE_PREFIX, StringConstants.DOT);
			DataSource dataSource = this.buildDataSource(resolver.getSubProperties(keyPrefix));
			this.dataBinder(dataSource);
			return dataSource;
		}

		private Map<String, DataSource> getTargetDataSources() {
			String keyPrefix =
				StringUtils.join(
					RoutingDataSourceDefault.PROP_SPRING_DATA_PROPS_DS_PREFIX, StringConstants.DOT);
			RelaxedPropertyResolver resolver = new RelaxedPropertyResolver(env, keyPrefix);

			String nodes = resolver.getProperty(RoutingDataSourceDefault.Properties.NODES);
			if (nodes == null) {
				return Collections.EMPTY_MAP;
			}

			String[] keys = nodes.split(StringConstants.COMMA);
			Map<String, DataSource> targetDataSources = Maps.newHashMapWithExpectedSize(keys.length + 1);
			for (String key : keys) {
				if (StringUtils.isBlank(key)) {
					continue;
				}

				Map<String, Object> properties =
					resolver.getSubProperties(StringUtils.join(key, StringConstants.DOT));
				targetDataSources.put(key, this.dataBinder(this.buildDataSource(properties)));
			}
			return targetDataSources;
		}

		@SuppressWarnings("unchecked")
		private DataSource buildDataSource(Map<String, Object> properties) {
			if (properties.containsKey(RoutingDataSourceDefault.Properties.JNDI_NAME)) {
				String jndiName = ObjectUtils.trimToString(
					properties.get(RoutingDataSourceDefault.Properties.JNDI_NAME));
				try {
					InitialContext initialContext = new InitialContext();
					return (DataSource) initialContext.lookup(jndiName);
				} catch(NamingException e) {
					throw new RuntimeException(e);
				}
			}

			Object type = properties.get(RoutingDataSourceDefault.Properties.TYPE);
			if (type == null) {
				type = RoutingDataSourceDefault.TYPE;
			}
			Class<? extends DataSource> dataSourceType;
			try {
				dataSourceType = (Class<? extends DataSource>) Class.forName((String) type);
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}

			return DataSourceBuilder.create()
				.type(dataSourceType)
				.driverClassName(
					ObjectUtils.trimToString(
						properties.get(RoutingDataSourceDefault.Properties.DRIVE_CLASS_NAME)))
				.url(ObjectUtils.trimToString(properties.get(RoutingDataSourceDefault.Properties.URL)))
				.username(
					ObjectUtils.trimToString(properties.get(RoutingDataSourceDefault.Properties.USERNAME)))
				.password(
					ObjectUtils.trimToString(properties.get(RoutingDataSourceDefault.Properties.PASSWORD)))
				.build();
		}

		private DataSource dataBinder(DataSource dataSource) {
			RelaxedDataBinder dataBinder = new RelaxedDataBinder(dataSource);
			dataBinder.setConversionService(conversionService);
			dataBinder.setIgnoreNestedProperties(false);
			dataBinder.setIgnoreInvalidFields(false);
			dataBinder.setIgnoreUnknownFields(true);
			if (propertyValues == null) {
				Map<String, Object> subProperties =
					new RelaxedPropertyResolver(env, DataConstants.PROP_DATASOURCE_PREFIX)
						.getSubProperties(StringConstants.DOT);
				HashMap<String, Object> properties = new HashMap<>();
				properties.putAll(subProperties);
				// 排除已经设置的属性
				properties.remove(RoutingDataSourceDefault.Properties.TYPE);
				properties.remove(RoutingDataSourceDefault.Properties.DRIVE_CLASS_NAME);
				properties.remove(RoutingDataSourceDefault.Properties.URL);
				properties.remove(RoutingDataSourceDefault.Properties.USERNAME);
				properties.remove(RoutingDataSourceDefault.Properties.PASSWORD);
				propertyValues = new MutablePropertyValues(properties);
			}
			dataBinder.bind(propertyValues);
			return dataSource;
		}
	}
}
