package org.ylzl.eden.spring.cloud.zipkin.autoconfigure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.spring.cloud.sleuth.web.WebMvcHandlerParser;

/**
 * 自定义 Brave 自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Configuration
public class CustomBraveAutoConfiguration {

	@Bean
	public WebMvcHandlerParser webMvcHandlerParser() {
		return new WebMvcHandlerParser();
	}
}
