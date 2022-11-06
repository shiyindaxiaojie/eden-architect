package org.ylzl.eden.full.tracing.spring.boot.autoconfigure;

import brave.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.full.tracing.integration.gateway.GatewayStressTagFilter;

/**
 * 压测标记网关自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
@Slf4j
@Configuration(proxyBeanMethods = false)
public class GatewayStressAutoConfiguration {

	@ConditionalOnMissingBean
	@Bean
	public GatewayStressTagFilter gatewayStressTagFilter(Tracer tracer) {
		return new GatewayStressTagFilter(tracer);
	}
}
