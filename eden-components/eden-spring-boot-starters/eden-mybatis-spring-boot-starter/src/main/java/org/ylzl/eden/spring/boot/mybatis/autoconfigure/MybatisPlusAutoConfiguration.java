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

package org.ylzl.eden.spring.boot.mybatis.autoconfigure;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusPropertiesCustomizer;
import com.baomidou.mybatisplus.autoconfigure.SpringBootVFS;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.incrementer.IKeyGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.type.TypeHandler;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Mybatis Plus 自定义自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@AutoConfigureBefore({
	MybatisAutoConfiguration.class,
	com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration.class
})
@ConditionalOnClass({
	SqlSessionFactory.class,
	SqlSessionFactoryBean.class,
	MybatisConfiguration.class
})
@EnableConfigurationProperties({MybatisPlusProperties.class})
@Slf4j
@Configuration
public class MybatisPlusAutoConfiguration {

	@ConditionalOnMissingBean
	@Bean
	public ConfigurationCustomizer configurationCustomizer() {
		return configuration -> configuration.setUseDeprecatedExecutor(false);
	}

	@Configuration
	public static class CustomMybatisPlusAutoConfiguration extends com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration {

		public static final String AUTOWIRED_MYBATIS_PLUS_SQL_SESSION_FACTORY = "Autowired MybatisPlus SqlSessionFactory";

		private static final String DEFAULT_MAPPER_LOCATION = "classpath*:/mybatis/mapper/**/*.xml";

		private static final ResourcePatternResolver RESOLVER =
			new PathMatchingResourcePatternResolver();

		private final MybatisPlusProperties properties;

		private final Interceptor[] interceptors;

		private final TypeHandler<?>[] typeHandlers;

		private final LanguageDriver[] languageDrivers;

		private final ResourceLoader resourceLoader;

		private final DatabaseIdProvider databaseIdProvider;

		private final List<ConfigurationCustomizer> configurationCustomizers;

		private final ApplicationContext applicationContext;

		public CustomMybatisPlusAutoConfiguration(
			MybatisPlusProperties properties,
			ObjectProvider<Interceptor[]> interceptorsProvider,
			ObjectProvider<TypeHandler[]> typeHandlersProvider,
			ObjectProvider<LanguageDriver[]> languageDriversProvider,
			ResourceLoader resourceLoader,
			ObjectProvider<DatabaseIdProvider> databaseIdProvider,
			ObjectProvider<List<ConfigurationCustomizer>> configurationCustomizersProvider,
			ObjectProvider<List<MybatisPlusPropertiesCustomizer>>
				mybatisPlusPropertiesCustomizerProvider,
			ApplicationContext applicationContext) {
			super(
				properties,
				interceptorsProvider,
				typeHandlersProvider,
				languageDriversProvider,
				resourceLoader,
				databaseIdProvider,
				configurationCustomizersProvider,
				mybatisPlusPropertiesCustomizerProvider,
				applicationContext);
			this.properties = properties;
			this.interceptors = interceptorsProvider.getIfAvailable();
			this.typeHandlers = typeHandlersProvider.getIfAvailable();
			this.languageDrivers = languageDriversProvider.getIfAvailable();
			this.resourceLoader = resourceLoader;
			this.databaseIdProvider = databaseIdProvider.getIfAvailable();
			this.configurationCustomizers = configurationCustomizersProvider.getIfAvailable();
			this.applicationContext = applicationContext;
			if (properties.getMapperLocations() == null || properties.getMapperLocations().length == 0) {
				properties.setMapperLocations(new String[]{DEFAULT_MAPPER_LOCATION});
			}
			properties.resolveMapperLocations();
		}

		@Override
		@Bean
		@ConditionalOnMissingBean
		public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
			log.debug(AUTOWIRED_MYBATIS_PLUS_SQL_SESSION_FACTORY);
			MybatisSqlSessionFactoryBean factory = new MybatisSqlSessionFactoryBean();
			factory.setDataSource(dataSource);
			factory.setVfs(SpringBootVFS.class);

			if (StringUtils.hasText(this.properties.getConfigLocation())) {
				factory.setConfigLocation(
					this.resourceLoader.getResource(this.properties.getConfigLocation()));
			}

			applyConfiguration(factory);
			if (this.properties.getConfigurationProperties() != null) {
				factory.setConfigurationProperties(this.properties.getConfigurationProperties());
			}
			if (!ObjectUtils.isEmpty(this.interceptors)) {
				factory.setPlugins(this.interceptors);
			}
			if (this.databaseIdProvider != null) {
				factory.setDatabaseIdProvider(this.databaseIdProvider);
			}
			if (StringUtils.hasLength(this.properties.getTypeAliasesPackage())) {
				factory.setTypeAliasesPackage(this.properties.getTypeAliasesPackage());
			}
			if (this.properties.getTypeAliasesSuperType() != null) {
				factory.setTypeAliasesSuperType(this.properties.getTypeAliasesSuperType());
			}
			if (StringUtils.hasLength(this.properties.getTypeHandlersPackage())) {
				factory.setTypeHandlersPackage(this.properties.getTypeHandlersPackage());
			}
			if (!ObjectUtils.isEmpty(this.typeHandlers)) {
				factory.setTypeHandlers(this.typeHandlers);
			}

			Resource[] mapperLocations = this.properties.resolveMapperLocations();
			if (!ObjectUtils.isEmpty(mapperLocations)) {
				factory.setMapperLocations(mapperLocations);
			} else {
				factory.setMapperLocations(RESOLVER.getResources(DEFAULT_MAPPER_LOCATION));
			}
			this.getBeanThen(TransactionFactory.class, factory::setTransactionFactory);

			Class<? extends LanguageDriver> defaultLanguageDriver =
				this.properties.getDefaultScriptingLanguageDriver();
			if (!ObjectUtils.isEmpty(this.languageDrivers)) {
				factory.setScriptingLanguageDrivers(this.languageDrivers);
			}
			Optional.ofNullable(defaultLanguageDriver)
				.ifPresent(factory::setDefaultScriptingLanguageDriver);

			if (StringUtils.hasLength(this.properties.getTypeEnumsPackage())) {
				factory.setTypeEnumsPackage(this.properties.getTypeEnumsPackage());
			}
			GlobalConfig globalConfig = this.properties.getGlobalConfig();
			this.getBeanThen(MetaObjectHandler.class, globalConfig::setMetaObjectHandler);
			this.getBeanThen(IKeyGenerator.class, i -> globalConfig.getDbConfig().setKeyGenerator(i));
			this.getBeanThen(ISqlInjector.class, globalConfig::setSqlInjector);
			this.getBeanThen(IdentifierGenerator.class, globalConfig::setIdentifierGenerator);
			factory.setGlobalConfig(globalConfig);
			return factory.getObject();
		}

		private <T> void getBeanThen(Class<T> clazz, Consumer<T> consumer) {
			if (this.applicationContext.getBeanNamesForType(clazz, false, false).length > 0) {
				consumer.accept(this.applicationContext.getBean(clazz));
			}
		}

		private void applyConfiguration(MybatisSqlSessionFactoryBean factory) {
			MybatisConfiguration configuration = this.properties.getConfiguration();
			if (configuration == null && !StringUtils.hasText(this.properties.getConfigLocation())) {
				configuration = new MybatisConfiguration();
			}
			if (configuration != null && !CollectionUtils.isEmpty(this.configurationCustomizers)) {
				for (ConfigurationCustomizer customizer : this.configurationCustomizers) {
					customizer.customize(configuration);
				}
			}
			factory.setConfiguration(configuration);
		}
	}
}
