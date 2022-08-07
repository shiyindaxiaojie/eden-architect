package org.ylzl.eden.spring.boot.cat.autoconfigure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.spring.integration.cat.integration.web.filter.HttpTraceCatFilter;

/**
 * Web 集成 CAT 自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@ConditionalOnBean(CatAutoConfiguration.class)
@ConditionalOnWebApplication
@RequiredArgsConstructor
@Slf4j
@Configuration
public class WebCatAutoConfiguration {

	@ConditionalOnMissingBean
	@Bean
	public FilterRegistrationBean<HttpTraceCatFilter> httpTraceCatFilter() {
		FilterRegistrationBean<HttpTraceCatFilter> registration =
			new FilterRegistrationBean<>(new HttpTraceCatFilter());
		registration.setName("http-trace-cat-filter");
		registration.addUrlPatterns("/*");
		registration.setOrder(1);
		return registration;
	}
}
