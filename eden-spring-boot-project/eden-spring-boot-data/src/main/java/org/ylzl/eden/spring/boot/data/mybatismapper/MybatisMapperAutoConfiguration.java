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

package org.ylzl.eden.spring.boot.data.mybatismapper;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;
import org.ylzl.eden.spring.boot.data.mybatis.mapper.FixedClassPathMapperScanner;
import tk.mybatis.mapper.autoconfigure.MapperAutoConfiguration;
import tk.mybatis.mapper.autoconfigure.MapperProperties;
import tk.mybatis.mapper.autoconfigure.MybatisProperties;
import tk.mybatis.spring.mapper.SpringBootBindUtil;

import javax.sql.DataSource;
import java.util.Arrays;

/**
 * Mybatis Mapper 自动配置
 *
 * @author gyl
 * @since 1.0.0
 */
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@AutoConfigureBefore(MybatisAutoConfiguration.class)
@ConditionalOnClass({
  SqlSessionFactory.class,
  SqlSessionFactoryBean.class,
  tk.mybatis.mapper.common.Mapper.class
})
@ConditionalOnBean(DataSource.class)
@EnableConfigurationProperties({MybatisProperties.class})
@Slf4j
@Configuration
public class MybatisMapperAutoConfiguration
    extends MapperAutoConfiguration.AutoConfiguredMapperScannerRegistrar {

  private static final String MSG_AUTOWIRED_MYBATIS_MAPPER = "Autowired Mybatis Mapper";

  private static final String EXP_AUTO_SCAN_DISABLED =
      "Mybatis Mapper auto scan package disabled: {}";

  private static final String PROP_MAPPERS = "mapper.mappers";

  private static final String PROP_NOT_EMPTY = "mapper.not-empty";

  private BeanFactory beanFactory;

  private ResourceLoader resourceLoader;

  private Environment environment;

  @Override
  public void registerBeanDefinitions(
      AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
    log.debug(MSG_AUTOWIRED_MYBATIS_MAPPER);

    FixedClassPathMapperScanner scanner = new FixedClassPathMapperScanner(registry);

    // 设置默认值，简化配置
    MapperProperties properties =
        SpringBootBindUtil.bind(environment, MapperProperties.class, MapperProperties.PREFIX);
    if (environment.getProperty(PROP_MAPPERS) == null) {
      properties.setMappers(Arrays.asList(new Class[] {tk.mybatis.mapper.common.Mapper.class}));
    }
    if (environment.getProperty(PROP_NOT_EMPTY) == null) {
      properties.setNotEmpty(false);
    }
    scanner.setMapperProperties(properties);

    try {
      if (this.resourceLoader != null) {
        scanner.setResourceLoader(this.resourceLoader);
      }

      scanner.setAnnotationClass(Mapper.class);
      scanner.registerFilters();
      scanner.doScan(StringUtils.toStringArray(AutoConfigurationPackages.get(this.beanFactory)));
    } catch (IllegalStateException ex) {
      log.error(EXP_AUTO_SCAN_DISABLED, ex.getMessage(), ex);
    }
  }

  @Override
  public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
    this.beanFactory = beanFactory;
  }

  @Override
  public void setEnvironment(Environment environment) {
    this.environment = environment;
  }

  @Override
  public void setResourceLoader(ResourceLoader resourceLoader) {
    this.resourceLoader = resourceLoader;
  }
}
