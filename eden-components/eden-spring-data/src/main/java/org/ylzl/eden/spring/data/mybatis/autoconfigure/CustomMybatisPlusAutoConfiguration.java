package org.ylzl.eden.spring.data.mybatis.autoconfigure;

import com.baomidou.mybatisplus.autoconfigure.*;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.incrementer.IKeyGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
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
import org.ylzl.eden.spring.data.mybatis.autoconfigure.CustomMybatisAutoConfiguration;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Mybatis Plus 自定义自动配置
 *
 * @author gyl
 * @since 0.0.1
 */
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@AutoConfigureBefore({
  MybatisAutoConfiguration.class,
  MybatisPlusAutoConfiguration.class,
  CustomMybatisAutoConfiguration.class
})
@ConditionalOnClass({
  SqlSessionFactory.class,
  SqlSessionFactoryBean.class,
  MybatisConfiguration.class
})
@EnableConfigurationProperties({MybatisPlusProperties.class})
@Slf4j
@Configuration
public class CustomMybatisPlusAutoConfiguration {

  @ConditionalOnMissingBean
  @Bean
  public MybatisPlusInterceptor mybatisPlusInterceptor() {
    MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
    interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
    return interceptor;
  }

  @ConditionalOnMissingBean
  @Bean
  public ConfigurationCustomizer configurationCustomizer() {
    return configuration -> configuration.setUseDeprecatedExecutor(false);
  }

  @Configuration
  public static class InnerMybatisPlusAutoConfiguration extends MybatisPlusAutoConfiguration {

    private static final String MSG_AUTOWIRED_MYBATIS_PLUS =
        "Autowired MybatisPlus SqlSessionFactory";

    private static final String DEFAULT_MAPPER_LOCATION = "classpath*:/mapper/**/*.xml";

    private static final ResourcePatternResolver RESOLVER =
        new PathMatchingResourcePatternResolver();

    private final MybatisPlusProperties properties;

    private final Interceptor[] interceptors;

    private final TypeHandler[] typeHandlers;

    private final LanguageDriver[] languageDrivers;

    private final ResourceLoader resourceLoader;

    private final DatabaseIdProvider databaseIdProvider;

    private final List<ConfigurationCustomizer> configurationCustomizers;

    private final List<MybatisPlusPropertiesCustomizer> mybatisPlusPropertiesCustomizers;

    private final ApplicationContext applicationContext;

    public InnerMybatisPlusAutoConfiguration(
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
      this.mybatisPlusPropertiesCustomizers =
          mybatisPlusPropertiesCustomizerProvider.getIfAvailable();
      this.applicationContext = applicationContext;
      if (properties.getMapperLocations() == null || properties.getMapperLocations().length == 0) {
        properties.setMapperLocations(new String[] {DEFAULT_MAPPER_LOCATION});
      }
      properties.resolveMapperLocations();
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
