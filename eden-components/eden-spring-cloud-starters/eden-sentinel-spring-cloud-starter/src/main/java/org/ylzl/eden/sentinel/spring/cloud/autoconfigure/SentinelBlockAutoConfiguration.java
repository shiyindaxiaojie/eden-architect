package org.ylzl.eden.sentinel.spring.cloud.autoconfigure;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.spring.cloud.sentinel.web.CustomBlockExceptionHandler;

/**
 * Sentinel 防护自定义异常自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ConditionalOnClass(BlockExceptionHandler.class)
@ConditionalOnProperty(name = "spring.cloud.sentinel.enabled", matchIfMissing = true)
@Slf4j
@Configuration(proxyBeanMethods = false)
public class SentinelBlockAutoConfiguration {

	public static final String AUTOWIRED_CUSTOM_BLOCK_EXCEPTION_HANDLER = "Autowired CustomBlockExceptionHandler";

	@Bean
	public CustomBlockExceptionHandler customBlockExceptionHandler() {
		log.debug(AUTOWIRED_CUSTOM_BLOCK_EXCEPTION_HANDLER);
		return new CustomBlockExceptionHandler();
	}
}
