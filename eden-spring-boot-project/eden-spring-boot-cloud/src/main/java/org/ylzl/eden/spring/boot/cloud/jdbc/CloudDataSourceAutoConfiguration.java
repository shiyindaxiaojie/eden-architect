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

package org.ylzl.eden.spring.boot.cloud.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.ylzl.eden.spring.boot.framework.core.ProfileConstants;

import javax.sql.DataSource;

/**
 * 云服务数据库自动配置
 *
 * @author gyl
 * @since 0.0.1
 */
@AutoConfigureBefore(name = {
    "org.ylzl.eden.spring.boot.data.jdbc.RoutingDataSourceConfiguration"
}, value = {
    DataSourceAutoConfiguration.class
})
@Profile(ProfileConstants.SPRING_PROFILE_CLOUD)
@Slf4j
@Configuration
public class CloudDataSourceAutoConfiguration extends AbstractCloudConfig {

	private static final String MSG_CLOUD_JDBC_DATA_SOURCE = "Inject JDBC datasource from a cloud provider";

    @ConditionalOnMissingBean
    @Bean
    public DataSource dataSource() {
        log.info(MSG_CLOUD_JDBC_DATA_SOURCE);
        return connectionFactory().dataSource();
    }
}
