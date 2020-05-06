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
package org.ylzl.eden.spring.boot.cloud.hystrix;

import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Hystrix 监控自动配置
 *
 * @author gyl
 * @since 0.0.1
 */
@ConditionalOnClass(HystrixMetricsStreamServlet.class)
@Slf4j
@Configuration
public class HystrixMetricsAutoConfiguration {

  private static final String MSG_INJECT_HYSTRIX_METRICS = "Inject Hystrix Metrics";

  private static final String HYSTRIX_BEAN_URL_MAPPINGS = "/hystrix.stream";

  private static final String HYSTRIX_BEAN_NAME = "HystrixMetricsStreamServlet";

  @ConditionalOnMissingBean
  @Bean
  public ServletRegistrationBean hystrixMetricsStreamServlet() {
    log.debug(MSG_INJECT_HYSTRIX_METRICS);
    HystrixMetricsStreamServlet streamServlet = new HystrixMetricsStreamServlet();
    ServletRegistrationBean registrationBean = new ServletRegistrationBean(streamServlet);
    registrationBean.setLoadOnStartup(1);
    registrationBean.addUrlMappings(HYSTRIX_BEAN_URL_MAPPINGS);
    registrationBean.setName(HYSTRIX_BEAN_NAME);
    return registrationBean;
  }
}
