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

package org.ylzl.eden.spring.data.mybatis.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.ylzl.eden.commons.lang.ObjectUtils;
import org.ylzl.eden.commons.lang.StringUtils;
import org.ylzl.eden.spring.data.mybatis.factory.FixedSqlSessionFactoryBean;
import org.ylzl.eden.spring.framework.core.convert.BeanCopierHelper;
import org.ylzl.eden.spring.framework.info.contributor.InfoContributorProvider;

import javax.sql.DataSource;
import java.util.List;

/**
 * Mybatis 自定义配置
 *
 * @author gyl
 * @since 1.0.0
 */
@ConditionalOnMissingClass("com.baomidou.mybatisplus.core.MybatisConfiguration")
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@AutoConfigureBefore({MybatisAutoConfiguration.class})
@ConditionalOnClass({SqlSessionFactory.class, SqlSessionFactoryBean.class})
@EnableConfigurationProperties({MybatisProperties.class})
@Slf4j
@Configuration
public class CustomMybatisAutoConfiguration {

  private static final String MSG_AUTOWIRED_MYBATIS_SQL_SESSION_FACTORY =
      "Autowired Mybatis SqlSessionFactory";

  private static final String DEFAULT_CONFIG_LOCATION = "config/mybatis/mybatis-config.xml";

  private static final String[] DEFAULT_MAPPER_LOCATIONS =
      new String[] {
        ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + "config/mybatis/mappers/**.xml"
      };

  private static final String DEFAULT_PACKAGE = "domain";

  private final MybatisProperties mybatisProperties;

  public CustomMybatisAutoConfiguration(MybatisProperties mybatisProperties) {
    this.mybatisProperties = mybatisProperties;
  }

  @ConditionalOnMissingBean
  @Bean
  public SqlSessionFactoryBean sqlSessionFactoryBean(
      DataSource dataSource,
      InfoContributorProvider infoContributorProvider,
      PathMatchingResourcePatternResolver resolver,
      @Autowired(required = false) List<Interceptor> interceptors) {
    log.debug(MSG_AUTOWIRED_MYBATIS_SQL_SESSION_FACTORY);
    SqlSessionFactoryBean sqlSessionFactoryBean = new FixedSqlSessionFactoryBean();
    BeanCopierHelper.copy(mybatisProperties, sqlSessionFactoryBean);
    sqlSessionFactoryBean.setDataSource(dataSource);

    if (StringUtils.isBlank(mybatisProperties.getConfigLocation())) {
      mybatisProperties.setConfigLocation(DEFAULT_CONFIG_LOCATION);
    }
    sqlSessionFactoryBean.setConfigLocation(
        resolver.getResource(mybatisProperties.getConfigLocation()));

    if (ObjectUtils.isNull(mybatisProperties.getMapperLocations())) {
      mybatisProperties.setMapperLocations(DEFAULT_MAPPER_LOCATIONS);
    }
    sqlSessionFactoryBean.setMapperLocations(mybatisProperties.resolveMapperLocations());

    if (StringUtils.isBlank((mybatisProperties.getTypeAliasesPackage()))) {
      mybatisProperties.setTypeAliasesPackage(
          infoContributorProvider.resolvePackage(DEFAULT_PACKAGE));
    }
    sqlSessionFactoryBean.setTypeAliasesPackage(mybatisProperties.getTypeAliasesPackage());

    if (interceptors != null && !interceptors.isEmpty()) {
      sqlSessionFactoryBean.setPlugins(interceptors.toArray(new Interceptor[interceptors.size()]));
    }
    return sqlSessionFactoryBean;
  }
}
