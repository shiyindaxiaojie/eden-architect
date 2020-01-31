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

package org.ylzl.eden.spring.boot.integration.flowable;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.image.impl.DefaultProcessDiagramGenerator;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.flowable.spring.boot.ProcessEngineConfigurationConfigurer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.spring.boot.framework.core.FrameworkConstants;

import javax.sql.DataSource;

/**
 * Flowable 自动配置
 *
 * @author gyl
 * @since 0.0.1
 */
@ConditionalOnClass(ProcessEngineConfigurationConfigurer.class)
@EnableConfigurationProperties(EnhancedFlowableProperties.class)
@Slf4j
@Configuration
public class EnhancedFlowableAutoConfiguration implements ProcessEngineConfigurationConfigurer {

	private final DataSource dataSource;

	public EnhancedFlowableAutoConfiguration(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public void configure(SpringProcessEngineConfiguration springProcessEngineConfiguration) {
		springProcessEngineConfiguration.setDataSource(dataSource);
		springProcessEngineConfiguration.setActivityFontName(FrameworkConstants.DEFAULT_FONT_NAME);
		springProcessEngineConfiguration.setLabelFontName(FrameworkConstants.DEFAULT_FONT_NAME);
		springProcessEngineConfiguration.setProcessDiagramGenerator(new DefaultProcessDiagramGenerator());
		springProcessEngineConfiguration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
	}
}
