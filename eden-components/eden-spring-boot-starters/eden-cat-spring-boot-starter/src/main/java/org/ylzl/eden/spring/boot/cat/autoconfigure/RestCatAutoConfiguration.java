package org.ylzl.eden.spring.boot.cat.autoconfigure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.ylzl.eden.spring.integration.cat.integration.rest.interceptor.RestTemplateTraceInterceptor;

import java.util.Collections;

/**
 * Rest 集成 CAT 自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
@ConditionalOnBean({
	CatAutoConfiguration.class,
	RestTemplate.class
})
@Slf4j
@Configuration(proxyBeanMethods = false)
public class RestCatAutoConfiguration implements InitializingBean {

	private final RestTemplate restTemplate;

	@Override
	public void afterPropertiesSet() {
		restTemplate.setInterceptors(Collections.singletonList(new RestTemplateTraceInterceptor()));
	}
}
