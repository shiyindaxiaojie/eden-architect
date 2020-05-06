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
package org.ylzl.eden.spring.boot.data.elasticsearch;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.Client;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.NodeClientFactoryBean;
import org.springframework.data.elasticsearch.client.TransportClientFactoryBean;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.ylzl.eden.spring.boot.data.elasticsearch.mapper.JacksonEntityMapper;

/**
 * Elasticsearch 数据自动配置
 *
 * @author gyl
 * @since 0.0.1
 */
@AutoConfigureAfter(ElasticsearchAutoConfiguration.class)
@AutoConfigureBefore(ElasticsearchDataAutoConfiguration.class)
@ConditionalOnClass({Client.class, TransportClientFactoryBean.class, NodeClientFactoryBean.class})
@EnableConfigurationProperties(ElasticsearchProperties.class)
@Slf4j
@Configuration
public class EnhancedElasticsearchDataAutoConfiguration {

  public static final String MSG_INJECT_ES_TEMPLATE = "Inject ElasticsearchTemplate with Jackson";

  @ConditionalOnMissingBean
  @Bean
  public ElasticsearchTemplate elasticsearchTemplate(
      Client client, Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder) {
    log.debug(MSG_INJECT_ES_TEMPLATE);
    return new ElasticsearchTemplate(
        client,
        new JacksonEntityMapper(jackson2ObjectMapperBuilder.createXmlMapper(false).build()));
  }
}
