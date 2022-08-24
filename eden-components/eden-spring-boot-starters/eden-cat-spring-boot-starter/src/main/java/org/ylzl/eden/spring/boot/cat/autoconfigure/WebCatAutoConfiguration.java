package org.ylzl.eden.spring.boot.cat.autoconfigure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.spring.integration.cat.integration.web.filter.CatHttpTraceFilter;

/**
 * Web 集成 CAT 自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ConditionalOnBean(CatAutoConfiguration.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@RequiredArgsConstructor
@Slf4j
@Configuration(proxyBeanMethods = false)
public class WebCatAutoConfiguration {

	public static final String AUTOWIRED_CAT_HTTP_TRACE_FILTER_FILTER = "Autowired CatHttpTraceFilterFilter";

	@Bean
	public FilterRegistrationBean<CatHttpTraceFilter> catHttpTraceFilterFilter() {
		log.debug(AUTOWIRED_CAT_HTTP_TRACE_FILTER_FILTER);
		FilterRegistrationBean<CatHttpTraceFilter> registration =
			new FilterRegistrationBean<>(new CatHttpTraceFilter());
		registration.setName("http-trace-cat-filter");
		registration.addUrlPatterns("/*");
		registration.setOrder(1);
		return registration;
	}
}
