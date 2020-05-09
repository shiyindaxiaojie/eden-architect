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

package org.ylzl.eden.spring.boot.data.neo4j;

import lombok.extern.slf4j.Slf4j;
import org.neo4j.ogm.session.Neo4jSession;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.neo4j.Neo4jDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.neo4j.Neo4jProperties;
import org.springframework.boot.autoconfigure.domain.EntityScanPackages;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.data.neo4j.template.Neo4jOperations;
import org.springframework.data.neo4j.transaction.Neo4jTransactionManager;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

/**
 * Neo4j 集成 JPA 自动配置
 *
 * @author gyl
 * @since 0.0.1
 */
@AutoConfigureBefore({
  DataSourceTransactionManagerAutoConfiguration.class,
  Neo4jDataAutoConfiguration.class
})
@ConditionalOnClass({Neo4jSession.class, Neo4jOperations.class})
@ConditionalOnMissingBean(Neo4jOperations.class)
@EnableAutoConfiguration(exclude = {Neo4jDataAutoConfiguration.class})
@EnableConfigurationProperties(Neo4jProperties.class)
@Slf4j
@Configuration
public class FixedNeo4jAutoConfiguration extends Neo4jConfiguration {

  public static final String DEFAULT_TM_BEAN_NAME = "transactionManager";

  public static final String JPA_TM_BEAN_NAME = "jpaTransactionManager";

  public static final String NEO4J_TM_BEAN_NAME = "neo4jTransactionManager";

  public static final String MSG_INJECT_TM = "装配 default TransactionManager";

  public static final String MSG_INJECT_JPA_TM = "装配 JpaTransactionManager";

  public static final String MSG_INJECT_NEO4J_TM = "装配 Neo4jTransactionManager";

  private final ApplicationContext applicationContext;

  private final Neo4jProperties neo4jProperties;

  public FixedNeo4jAutoConfiguration(
      ApplicationContext applicationContext, Neo4jProperties neo4jProperties) {
    this.applicationContext = applicationContext;
    this.neo4jProperties = neo4jProperties;
  }

  @Override
  public SessionFactory getSessionFactory() {
    return new SessionFactory(configuration(), getPackagesToScan());
  }

  @Scope(
      scopeName = "${spring.data.neo4j.session.scope:singleton}",
      proxyMode = ScopedProxyMode.TARGET_CLASS)
  @Bean
  @Override
  public Session getSession() throws Exception {
    return super.getSession();
  }

  @ConditionalOnMissingBean
  @Bean
  public org.neo4j.ogm.config.Configuration configuration() {
    return this.neo4jProperties.createConfiguration();
  }

  @Primary
  @Bean(name = JPA_TM_BEAN_NAME)
  public JpaTransactionManager jpaTransactionManager(
      LocalContainerEntityManagerFactoryBean entityManagerFactory) {
    log.debug(MSG_INJECT_JPA_TM);
    return new JpaTransactionManager(entityManagerFactory.getObject());
  }

  @Bean(name = NEO4J_TM_BEAN_NAME)
  public Neo4jTransactionManager neo4jTransactionManager(Session session) {
    log.debug(MSG_INJECT_NEO4J_TM);
    return new Neo4jTransactionManager(session);
  }

  @Bean(name = DEFAULT_TM_BEAN_NAME)
  public PlatformTransactionManager platformTransactionManager(
      Neo4jTransactionManager neo4jTransactionManager,
      JpaTransactionManager jpaTransactionManager) {
    log.debug(MSG_INJECT_TM);
    return new ChainedTransactionManager(jpaTransactionManager, neo4jTransactionManager);
  }

  private String[] getPackagesToScan() {
    List<String> packages = EntityScanPackages.get(this.applicationContext).getPackageNames();
    if (packages.isEmpty() && AutoConfigurationPackages.has(this.applicationContext)) {
      packages = AutoConfigurationPackages.get(this.applicationContext);
    }
    return packages.toArray(new String[packages.size()]);
  }
}
