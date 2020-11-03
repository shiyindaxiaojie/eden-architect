package org.ylzl.eden.spring.boot.data.mybatisplus;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.autoconfigure.SpringBootVFS;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.incrementer.IKeyGenerator;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.injector.LogicSqlInjector;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
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
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.ylzl.eden.spring.boot.data.mybatis.CustomMybatisAutoConfiguration;

import javax.sql.DataSource;
import java.util.Iterator;
import java.util.List;

/**
 * Mybatis Plus 自定义自动配置
 *
 * @author gyl
 * @since 0.0.1
 */
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@AutoConfigureBefore({MybatisAutoConfiguration.class, MybatisPlusAutoConfiguration.class, CustomMybatisAutoConfiguration.class})
@ConditionalOnClass({SqlSessionFactory.class, SqlSessionFactoryBean.class, MybatisConfiguration.class})
@EnableConfigurationProperties({MybatisPlusProperties.class})
@Slf4j
@Configuration
public class CustomMybatisPlusAutoConfiguration {

	@Configuration
	public static class InnerMybatisPlusAutoConfiguration extends MybatisPlusAutoConfiguration {

		private static final String MSG_AUTOWIRED_MYBATIS_PLUS =
			"Autowired MybatisPlus SqlSessionFactory";

		private static final String DEFAULT_MAPPER_LOCATION = "classpath*:/mybatis/**/*.xml";

		private static final ResourcePatternResolver RESOLVER = new PathMatchingResourcePatternResolver();

		private final MybatisPlusProperties properties;

		private final Interceptor[] interceptors;

		private final ResourceLoader resourceLoader;

		private final DatabaseIdProvider databaseIdProvider;

		private final List<ConfigurationCustomizer> configurationCustomizers;

		private final ApplicationContext applicationContext;

		public InnerMybatisPlusAutoConfiguration(MybatisPlusProperties properties, ObjectProvider<Interceptor[]> interceptorsProvider,
																							ResourceLoader resourceLoader, ObjectProvider<DatabaseIdProvider> databaseIdProvider,
																							ObjectProvider<List<ConfigurationCustomizer>> configurationCustomizersProvider,
																							ApplicationContext applicationContext) {
			super(properties, interceptorsProvider, resourceLoader, databaseIdProvider, configurationCustomizersProvider, applicationContext);
			this.properties = properties;
			this.interceptors = interceptorsProvider.getIfAvailable();
			this.resourceLoader = resourceLoader;
			this.databaseIdProvider = databaseIdProvider.getIfAvailable();
			this.configurationCustomizers = configurationCustomizersProvider.getIfAvailable();
			this.applicationContext = applicationContext;
		}

		@Override
		@Bean
		@ConditionalOnMissingBean
		public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
			log.debug(MSG_AUTOWIRED_MYBATIS_PLUS);
			MybatisSqlSessionFactoryBean factory = new MybatisSqlSessionFactoryBean();
			factory.setDataSource(dataSource);
			factory.setVfs(SpringBootVFS.class);
			if (StringUtils.hasText(this.properties.getConfigLocation())) {
				factory.setConfigLocation(this.resourceLoader.getResource(this.properties.getConfigLocation()));
			}

			this.applyConfiguration(factory);
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

			if (StringUtils.hasLength(this.properties.getTypeEnumsPackage())) {
				factory.setTypeEnumsPackage(this.properties.getTypeEnumsPackage());
			}

			if (this.properties.getTypeAliasesSuperType() != null) {
				factory.setTypeAliasesSuperType(this.properties.getTypeAliasesSuperType());
			}

			if (StringUtils.hasLength(this.properties.getTypeHandlersPackage())) {
				factory.setTypeHandlersPackage(this.properties.getTypeHandlersPackage());
			}

			if (!ObjectUtils.isEmpty(this.properties.resolveMapperLocations())) {
				factory.setMapperLocations(this.properties.resolveMapperLocations());
			} else {
				factory.setMapperLocations(RESOLVER.getResources(DEFAULT_MAPPER_LOCATION));
			}

			GlobalConfig globalConfig = this.properties.getGlobalConfig();
			if (this.applicationContext.getBeanNamesForType(MetaObjectHandler.class, false, false).length > 0) {
				MetaObjectHandler metaObjectHandler = this.applicationContext.getBean(MetaObjectHandler.class);
				globalConfig.setMetaObjectHandler(metaObjectHandler);
			}

			if (this.applicationContext.getBeanNamesForType(IKeyGenerator.class, false, false).length > 0) {
				IKeyGenerator keyGenerator = this.applicationContext.getBean(IKeyGenerator.class);
				globalConfig.getDbConfig().setKeyGenerator(keyGenerator);
			}

			if (this.applicationContext.getBeanNamesForType(ISqlInjector.class, false, false).length > 0) {
				ISqlInjector iSqlInjector = this.applicationContext.getBean(ISqlInjector.class);
				globalConfig.setSqlInjector(iSqlInjector);
			}

			factory.setGlobalConfig(globalConfig);
			return factory.getObject();
		}

		private void applyConfiguration(MybatisSqlSessionFactoryBean factory) {
			MybatisConfiguration configuration = this.properties.getConfiguration();
			if (configuration == null && !StringUtils.hasText(this.properties.getConfigLocation())) {
				configuration = new MybatisConfiguration();
			}

			if (configuration != null && !CollectionUtils.isEmpty(this.configurationCustomizers)) {
				Iterator var3 = this.configurationCustomizers.iterator();
				while (var3.hasNext()) {
					ConfigurationCustomizer customizer = (ConfigurationCustomizer) var3.next();
					customizer.customize(configuration);
				}
			}
			factory.setConfiguration(configuration);
		}
	}

	@Bean
	public ISqlInjector sqlInjector() {
		return new LogicSqlInjector();
	}

	@Bean
	public PaginationInterceptor paginationInterceptor() {
		return new PaginationInterceptor();
	}
}
