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

package org.ylzl.eden.dynamic.datasource.spring.boot.autoconfigure;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.dynamic.datasource.aop.DynamicDataSourceAnnotationAdvisor;
import com.baomidou.dynamic.datasource.aop.DynamicDataSourceAnnotationInterceptor;
import com.baomidou.dynamic.datasource.processor.DsProcessor;
import com.baomidou.dynamic.datasource.provider.DynamicDataSourceProvider;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDatasourceAopProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.Advisor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Role;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.ylzl.eden.dynamic.datasource.spring.boot.custom.CustomDynamicDataSourceAnnotationInterceptor;
import org.ylzl.eden.dynamic.datasource.spring.boot.custom.CustomDynamicRoutingDataSource;
import org.ylzl.eden.spring.boot.bootstrap.constant.Conditions;

import javax.sql.DataSource;

/**
 * 多数据源组合配置（sharding-jdbc、baomidou-dynamic）
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ConditionalOnProperty(
	prefix = "spring.datasource.dynamic",
	name = Conditions.ENABLED,
	havingValue = Conditions.TRUE,
	matchIfMissing = true
)
@ConditionalOnClass(DynamicDataSourceProvider.class)
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
@Configuration(proxyBeanMethods = false)
public class DynamicDataSourceAutoConfiguration {

	private static final String AUTOWIRED_DYNAMIC_ROUTING_DATA_SOURCE = "Autowired DynamicRoutingDataSource";

	private static final String AUTOWIRED_DYNAMIC_DATASOURCE_ANNOTATION_ADVISOR = "Autowired DynamicDatasourceAnnotationAdvisor";

	private final DynamicDataSourceProperties properties;

	public DynamicDataSourceAutoConfiguration(DynamicDataSourceProperties properties) {
		this.properties = properties;
	}

	@Primary
	@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
	@Bean
	public DataSource dataSource() {
		log.info(AUTOWIRED_DYNAMIC_ROUTING_DATA_SOURCE);
		DynamicRoutingDataSource dataSource = new CustomDynamicRoutingDataSource();
		dataSource.setPrimary(properties.getPrimary());
		dataSource.setStrict(properties.getStrict());
		dataSource.setStrategy(properties.getStrategy());
		dataSource.setP6spy(properties.getP6spy());
		dataSource.setSeata(properties.getSeata());
		return dataSource;
	}

	@ConditionalOnProperty(prefix = DynamicDataSourceProperties.PREFIX + ".aop", name = "enabled", havingValue = "true", matchIfMissing = true)
	@Primary
	@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
	@Bean
	public Advisor dynamicDatasourceAnnotationAdvisor(DsProcessor dsProcessor) {
		log.debug(AUTOWIRED_DYNAMIC_DATASOURCE_ANNOTATION_ADVISOR);
		DynamicDatasourceAopProperties aopProperties = properties.getAop();
		DynamicDataSourceAnnotationInterceptor interceptor =
			new CustomDynamicDataSourceAnnotationInterceptor(aopProperties.getAllowedPublicOnly(), dsProcessor);
		DynamicDataSourceAnnotationAdvisor advisor = new DynamicDataSourceAnnotationAdvisor(interceptor, DS.class);
		advisor.setOrder(aopProperties.getOrder());
		return advisor;
	}
}
