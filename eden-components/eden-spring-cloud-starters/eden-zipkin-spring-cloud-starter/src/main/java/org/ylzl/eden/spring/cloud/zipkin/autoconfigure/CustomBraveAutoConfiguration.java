package org.ylzl.eden.spring.cloud.zipkin.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.spring.cloud.sleuth.web.WebMvcHandlerParser;

/**
 * 自定义 Brave 自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@ConditionalOnWebApplication
@Slf4j
@Configuration
public class CustomBraveAutoConfiguration {

	public static final String AUTOWIRED_WEB_MVC_HANDLER_PARSER = "Autowired WebMvcHandlerParser";

	@Bean
	public WebMvcHandlerParser webMvcHandlerParser() {
		log.debug(AUTOWIRED_WEB_MVC_HANDLER_PARSER);
		return new WebMvcHandlerParser();
	}
}
