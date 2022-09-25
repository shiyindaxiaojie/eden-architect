package org.ylzl.eden.full.link.stress.testing.web.autoconfigure;

import brave.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.full.link.stress.testing.web.filter.WebStressTagFilter;

/**
 * 压测标记网关自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
@Slf4j
@Configuration(proxyBeanMethods = false)
public class WebStressAutoConfiguration {

	@ConditionalOnMissingBean
	@Bean
	public WebStressTagFilter webStressTagFilter(Tracer tracer) {
		return new WebStressTagFilter(tracer);
	}
}
