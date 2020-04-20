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
import com.codahale.metrics.jvm.*;
import com.ryantenney.metrics.spring.config.annotation.EnableMetrics;
import com.ryantenney.metrics.spring.config.annotation.MetricsConfigurerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.metrics.JmxReporter;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.spring.boot.integration.core.IntegrationConstants;

import javax.annotation.PostConstruct;
import java.lang.management.ManagementFactory;
import java.util.concurrent.TimeUnit;

/**
 * Metrics 自动配置
 *
 * <p>变更日志：Spring Boot 1.X 升级到 2.X</p>
 * <ul>
 *     <li>{@link com.ryantenney.metrics.spring.config.annotation.DelegatingMetricsConfiguration} 默认装配了 {@link MetricRegistry} 和 {@link HealthCheckRegistry}</li>
 * </ul>
 *
 * @see com.ryantenney.metrics.spring.config.annotation.DelegatingMetricsConfiguration
 * @author gyl
 * @since 1.0.0
 */
@ConditionalOnClass({MetricRegistry.class, HealthCheckRegistry.class})
@ConditionalOnExpression(MetricsAutoConfiguration.EXPS_METRICS_ENABLED)
@EnableConfigurationProperties(MetricsProperties.class)
@EnableMetrics(proxyTargetClass = true)
@Slf4j
@Configuration
public class MetricsAutoConfiguration extends MetricsConfigurerAdapter {

    public static final String EXPS_METRICS_ENABLED = "${" + IntegrationConstants.PROP_PREFIX + ".metrics.enabled:true}";

    public static final String LOGGER_NAME = "metrics";

    private static final String MSG_INJECT_JVM_GAUGE = "Inject Metrics JVM gauge";

    private static final String MSG_INJECT_JMX_REPORT = "Inject Metrics JMX reporting";

    private static final String MSG_INJECT_MSG_REPORT = "Inject Metrics log reporting";

    private static final String PROP_METRIC_REG_JVM_MEMORY = "jvm.memory";

    private static final String PROP_METRIC_REG_JVM_GARBAGE = "jvm.garbage";

    private static final String PROP_METRIC_REG_JVM_THREADS = "jvm.threads";

    private static final String PROP_METRIC_REG_JVM_FILES = "jvm.files";

    private static final String PROP_METRIC_REG_JVM_BUFFERS = "jvm.buffers";

    private final MetricRegistry metricRegistry;

    private final HealthCheckRegistry healthCheckRegistry;

    private final MetricsProperties metricsProperties;

	public MetricsAutoConfiguration(MetricRegistry metricRegistry, HealthCheckRegistry healthCheckRegistry, MetricsProperties metricsProperties) {
		this.metricRegistry = metricRegistry;
		this.healthCheckRegistry = healthCheckRegistry;
		this.metricsProperties = metricsProperties;
	}

    @Override
    public MetricRegistry getMetricRegistry() {
        return metricRegistry;
    }

    @Override
    public HealthCheckRegistry getHealthCheckRegistry() {
        return healthCheckRegistry;
    }

    @PostConstruct
    public void init() {
        log.debug(MSG_INJECT_JVM_GAUGE);
        metricRegistry.register(PROP_METRIC_REG_JVM_MEMORY, new MemoryUsageGaugeSet());
        metricRegistry.register(PROP_METRIC_REG_JVM_GARBAGE, new GarbageCollectorMetricSet());
        metricRegistry.register(PROP_METRIC_REG_JVM_THREADS, new ThreadStatesGaugeSet());
        metricRegistry.register(PROP_METRIC_REG_JVM_FILES, new FileDescriptorRatioGauge());
        metricRegistry.register(PROP_METRIC_REG_JVM_BUFFERS, new BufferPoolMetricSet(ManagementFactory.getPlatformMBeanServer()));
        /*if (metricsProperties.getJmx().isEnabled()) {
            log.debug(MSG_INJECT_JMX_REPORT);
            JmxReporter jmxReporter = JmxReporter.forRegistry(metricRegistry).build();
            jmxReporter.start();
        }*/
        if (metricsProperties.getLogs().isEnabled()) {
            log.info(MSG_INJECT_MSG_REPORT);
            final Slf4jReporter reporter = Slf4jReporter.forRegistry(metricRegistry)
                .outputTo(LoggerFactory.getLogger(LOGGER_NAME))
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build();
            reporter.start(metricsProperties.getLogs().getReportFrequency(), TimeUnit.SECONDS);
        }
    }
}
