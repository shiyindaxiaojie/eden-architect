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

package org.ylzl.eden.spring.boot.integration.metrics;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Slf4jReporter;
import com.codahale.metrics.health.HealthCheckRegistry;
import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.dropwizard.DropwizardConfig;
import io.micrometer.core.instrument.dropwizard.DropwizardMeterRegistry;
import io.micrometer.core.instrument.util.HierarchicalNameMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.spring.boot.integration.core.IntegrationConstants;

import java.util.concurrent.TimeUnit;

/**
 * Metrics 自动配置
 *
 * <p>从 Spring Boot 1.X 升级到 2.X
 *
 * <ul>
 *   <li>{@link com.ryantenney.metrics.spring.config.annotation.DelegatingMetricsConfiguration}
 *       默认装配了 {@link MetricRegistry} 和 {@link HealthCheckRegistry}
 * </ul>
 *
 * @author gyl
 * @see com.ryantenney.metrics.spring.config.annotation.DelegatingMetricsConfiguration
 * @since 1.0.0
 */
@ConditionalOnExpression(MetricsLoggingAutoConfiguration.EXPS_METRICS_LOGS_ENABLED)
@EnableConfigurationProperties(MetricsProperties.class)
@Slf4j
@Configuration
public class MetricsLoggingAutoConfiguration {

  public static final String EXPS_METRICS_LOGS_ENABLED =
      "${" + IntegrationConstants.PROP_PREFIX + ".metrics.logs.enabled:false}";

  public static final String LOGGER_NAME = "metrics";

  private static final String LOGGER_PREFIX = "console";

  private static final String MSG_AUTOWIRED_METRICS_REGISTRY_DROPWIZARD =
      "Autowired MetricRegistry (dropwizard)";

  private static final String MSG_AUTOWIRED_SLF4J_REPORTER_CONSOLE =
      "Autowired Slf4jReporter (console)";

  private static final String MSG_AUTOWIRED_METER_REGISTRY_CONSOLE =
      "Autowired MeterRegistry (console)";

  private final MetricsProperties metricsProperties;

  public MetricsLoggingAutoConfiguration(MetricsProperties metricsProperties) {
    this.metricsProperties = metricsProperties;
  }

  @Bean
  public MetricRegistry dropwizardMetricRegistry() {
    log.debug(MSG_AUTOWIRED_METRICS_REGISTRY_DROPWIZARD);
    return new MetricRegistry();
  }

  @Bean
  public Slf4jReporter consoleSlf4jReporter(MetricRegistry dropwizardMetricRegistry) {
    log.debug(MSG_AUTOWIRED_SLF4J_REPORTER_CONSOLE);
    Marker metricsMarker = MarkerFactory.getMarker(LOGGER_NAME);
    final Slf4jReporter reporter =
        Slf4jReporter.forRegistry(dropwizardMetricRegistry)
            .outputTo(LoggerFactory.getLogger(LOGGER_NAME))
            .markWith(metricsMarker)
            .convertRatesTo(TimeUnit.SECONDS)
            .convertDurationsTo(TimeUnit.MILLISECONDS)
            .build();
    reporter.start(metricsProperties.getLogs().getReportFrequency(), TimeUnit.SECONDS);
    return reporter;
  }

  @Bean
  public MeterRegistry consoleMeterRegistry(MetricRegistry dropwizardMetricRegistry) {
    log.debug(MSG_AUTOWIRED_METER_REGISTRY_CONSOLE);
    DropwizardConfig dropwizardConfig =
        new DropwizardConfig() {

          @Override
          public String prefix() {
            return LOGGER_PREFIX;
          }

          @Override
          public String get(String key) {
            return null;
          }
        };

    return new DropwizardMeterRegistry(
        dropwizardConfig, dropwizardMetricRegistry, HierarchicalNameMapper.DEFAULT, Clock.SYSTEM) {

      @Override
      protected Double nullGaugeValue() {
        return null;
      }
    };
  }
}
