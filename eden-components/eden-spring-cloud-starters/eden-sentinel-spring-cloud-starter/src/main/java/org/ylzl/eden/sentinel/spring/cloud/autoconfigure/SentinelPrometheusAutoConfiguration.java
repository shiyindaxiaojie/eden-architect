package org.ylzl.eden.sentinel.spring.cloud.autoconfigure;

import io.prometheus.client.CollectorRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.metrics.export.ConditionalOnEnabledMetricsExport;
import org.springframework.boot.actuate.autoconfigure.metrics.export.prometheus.PrometheusMetricsExportAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.spring.cloud.sentinel.prometheus.SentinelCollectorRegistry;

/**
 * Sentinel 集成 Prometheus 自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@AutoConfigureBefore({ PrometheusMetricsExportAutoConfiguration.class })
@AutoConfigureAfter(MetricsAutoConfiguration.class)
@ConditionalOnClass(CollectorRegistry.class)
@ConditionalOnEnabledMetricsExport("prometheus")
@ConditionalOnProperty(name = "spring.cloud.sentinel.enabled", matchIfMissing = true)
@Slf4j
@Configuration(proxyBeanMethods = false)
public class SentinelPrometheusAutoConfiguration {

	public static final String AUTOWIRED_SENTINEL_COLLECTOR_REGISTRY = "Autowired SentinelCollectorRegistry";

	@Bean
	public SentinelCollectorRegistry sentinelCollectorRegistry(CollectorRegistry registry) {
		log.debug(AUTOWIRED_SENTINEL_COLLECTOR_REGISTRY);
		return new SentinelCollectorRegistry(registry);
	}
}
