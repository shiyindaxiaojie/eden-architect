package org.ylzl.eden.full.tracing.spring.boot.autoconfigure;

import brave.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.full.tracing.integration.web.WebStressTagFilter;

/**
 * 压测标记网关自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ConditionalOnProperty(value = WebStressAutoConfiguration.ENABLED, havingValue = "true")
@RequiredArgsConstructor
@Slf4j
@Configuration(proxyBeanMethods = false)
public class WebStressAutoConfiguration {

	public static final String ENABLED ="stress.web.enabled";

	@ConditionalOnMissingBean
	@Bean
	public WebStressTagFilter webStressTagFilter(Tracer tracer) {
		return new WebStressTagFilter(tracer);
	}
}
