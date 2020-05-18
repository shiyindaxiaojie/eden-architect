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

package org.ylzl.eden.spring.boot.data.mongobee;

import com.github.mongobee.Mongobee;
import com.mongodb.MongoClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.ylzl.eden.spring.boot.data.mongobee.async.AsyncMongobee;
import org.ylzl.eden.spring.boot.framework.info.InfoContributorProvider;
import org.ylzl.eden.spring.boot.framework.scheduling.AsyncTaskExecutorAutoConfiguration;

/**
 * MongoDB 配置
 *
 * @author gyl
 * @since 1.0.0
 */
@ConditionalOnClass({Mongobee.class})
@ConditionalOnProperty(prefix = "mongobee", name = "enabled", matchIfMissing = true)
@EnableConfigurationProperties(MongobeeProperties.class)
@Import(MongoAutoConfiguration.class)
@Slf4j
@Configuration
public class MongobeeAutoConfiguration {

  private static final String DEFAULT_CONFIG_DBMIGRATIONS_SUFFIX = "config.dbmigrations";

  private static final String MSG_INJECT_MONGOBEE = "Inject Mongobee";

  private final Environment environment;

  private final MongobeeProperties mongobeeProperties;

  public MongobeeAutoConfiguration(Environment environment, MongobeeProperties mongobeeProperties) {
    this.environment = environment;
    this.mongobeeProperties = mongobeeProperties;
  }

  @Bean
  public LocalValidatorFactoryBean localValidator() {
    return new LocalValidatorFactoryBean();
  }

  @Bean
  public ValidatingMongoEventListener validatingMongoEventListener() {
    return new ValidatingMongoEventListener(localValidator());
  }

  @Bean
  public Mongobee mongobee(
      @Qualifier(AsyncTaskExecutorAutoConfiguration.BEAN_TASK_EXECUTOR) TaskExecutor taskExecutor,
      InfoContributorProvider infoContributorProvider,
      MongoClient mongoClient,
      MongoTemplate mongoTemplate,
      MongoProperties mongoProperties) {
    log.debug(MSG_INJECT_MONGOBEE);
    Mongobee mongobee = new AsyncMongobee(mongoClient, environment, taskExecutor);
    mongobee.setDbName(mongoProperties.getMongoClientDatabase());
    mongobee.setMongoTemplate(mongoTemplate);
    if (StringUtils.isNotBlank(mongobeeProperties.getChangeLogsScanPackage())) {
      mongobee.setChangeLogsScanPackage(mongobeeProperties.getChangeLogsScanPackage());
    } else {
      mongobee.setChangeLogsScanPackage(
          infoContributorProvider.resolvePackage(DEFAULT_CONFIG_DBMIGRATIONS_SUFFIX));
    }
    mongobee.setEnabled(mongobeeProperties.getEnabled());
    return mongobee;
  }
}
