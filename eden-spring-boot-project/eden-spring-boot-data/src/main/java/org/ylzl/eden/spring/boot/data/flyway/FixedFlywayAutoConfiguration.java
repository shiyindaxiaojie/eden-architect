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

package org.ylzl.eden.spring.boot.data.flyway;

import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.jpa.EntityManagerFactoryDependsOnPostProcessor;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayDataSource;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationInitializer;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.orm.jpa.AbstractEntityManagerFactoryBean;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.util.Assert;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;
import org.ylzl.eden.spring.boot.commons.bean.BeanCopier;
import org.ylzl.eden.spring.boot.data.flyway.async.AsyncFlyway;
import org.ylzl.eden.spring.boot.data.flyway.datasource.FlywaySQLiteDataSource;
import org.ylzl.eden.spring.boot.framework.scheduling.AsyncTaskExecutorAutoConfiguration;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

/**
 * Flyway 自动配置
 *
 * @author gyl
 * @since 0.0.1
 */
@AutoConfigureBefore(FlywayAutoConfiguration.class)
@ConditionalOnClass({Flyway.class})
@ConditionalOnProperty(prefix = "flyway", name = "enabled", matchIfMissing = true)
@EnableConfigurationProperties({FixedFlywayProperties.class})
@Slf4j
@Configuration
public class FixedFlywayAutoConfiguration {

    @AutoConfigureBefore(DataSourceAutoConfiguration.class)
    @ConditionalOnClass({DataSource.class})
    @Configuration
    public static class FixedFlywaySQLiteConfiguration {

        @ConditionalOnMissingBean
        @Bean
        public DataSource fixedSQLiteDataSource(DataSourceProperties dataSourceProperties) {
            SQLiteConfig sqLiteConfig = new SQLiteConfig();
            sqLiteConfig.setJournalMode(SQLiteConfig.JournalMode.WAL);
            sqLiteConfig.setSynchronous(SQLiteConfig.SynchronousMode.NORMAL);
            SQLiteDataSource dataSource = new SQLiteDataSource(sqLiteConfig);
            dataSource.setUrl(dataSourceProperties.getUrl());
            return new FlywaySQLiteDataSource(dataSource);
        }
    }

    @AutoConfigureAfter({
        DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class
    })
	@ConditionalOnClass({AsyncTaskExecutor.class})
    @EnableConfigurationProperties(FixedFlywayProperties.class)
    @Configuration
    public static class FlywayConfiguration {

        private static final String MSG_INJECT_LIQUIBASE = "Inject Flyway";

        public static final String DEFAULT_LOCATIONS = "classpath:config/flyway/db/migration";

        private final FixedFlywayProperties properties;

        private final ResourceLoader resourceLoader;

        private final DataSource dataSource;

        private final FlywayMigrationStrategy migrationStrategy;

        public FlywayConfiguration(FixedFlywayProperties properties,
                                   ResourceLoader resourceLoader,
                                   ObjectProvider<DataSource> dataSourceProvider,
                                   @FlywayDataSource ObjectProvider<DataSource> flywayDataSourceProvider,
                                   ObjectProvider<FlywayMigrationStrategy> migrationStrategyProvider) {
            this.properties = properties;
            this.resourceLoader = resourceLoader;
            this.dataSource = dataSourceProvider.getIfUnique();
            this.migrationStrategy = migrationStrategyProvider.getIfAvailable();
        }

        @ConditionalOnMissingBean
        @Bean
        public Flyway flyway(@Qualifier(AsyncTaskExecutorAutoConfiguration.BEAN_TASK_EXECUTOR) AsyncTaskExecutor asyncTaskExecutor,
                             @Value("${flyway.locations:" + DEFAULT_LOCATIONS + "}") String[] locations, Environment environment) {
            log.debug(MSG_INJECT_LIQUIBASE);
            Flyway flyway = new AsyncFlyway(asyncTaskExecutor, environment);
            flyway.setDataSource(dataSource);
            flyway.setLocations(locations);
            BeanCopier.copy(properties, flyway);
            return flyway;
        }

        @Bean
        @ConditionalOnMissingBean
        public FlywayMigrationInitializer flywayInitializer(Flyway flyway) {
            return new FlywayMigrationInitializer(flyway, this.migrationStrategy);
        }

        @ConditionalOnClass(LocalContainerEntityManagerFactoryBean.class)
        @ConditionalOnBean(AbstractEntityManagerFactoryBean.class)
        @Configuration
        protected static class FlywayJpaDependencyConfiguration extends EntityManagerFactoryDependsOnPostProcessor {
            public FlywayJpaDependencyConfiguration() {
                super("flyway");
            }

        }

        @PostConstruct
        public void checkLocationExists() {
            if (this.properties.isCheckLocation()) {
                Assert.state(!this.properties.getLocations().isEmpty(), "Migration script locations not configured");
                boolean exists = hasAtLeastOneLocation();
                Assert.state(exists, "Cannot find migrations location in: " + this.properties.getLocations()
                        + " (please add migrations or check your Flyway configuration)");
            }
        }

        private boolean hasAtLeastOneLocation() {
            for (String location : this.properties.getLocations()) {
                if (this.resourceLoader.getResource(location).exists()) {
                    return true;
                }
            }
            return false;
        }
    }
}
