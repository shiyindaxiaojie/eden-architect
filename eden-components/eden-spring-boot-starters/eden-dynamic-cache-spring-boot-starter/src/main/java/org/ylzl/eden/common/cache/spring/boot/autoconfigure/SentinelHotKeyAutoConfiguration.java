package org.ylzl.eden.common.cache.spring.boot.autoconfigure;

import com.alibaba.csp.sentinel.SphU;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.ylzl.eden.common.cache.integration.hotkey.sentinel.SentinelHotKeyClient;
import org.ylzl.eden.common.cache.spring.boot.env.CacheProperties;
import org.ylzl.eden.spring.boot.bootstrap.constant.Conditions;

/**
 * SentinelHotKey 自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ConditionalOnExpression("${spring.cloud.sentinel.enabled:true}")
@ConditionalOnProperty(
	prefix = CacheProperties.PREFIX + ".dynamic.hotkey.sentinel",
	name = Conditions.ENABLED,
	havingValue = Conditions.TRUE,
	matchIfMissing = true
)
@ConditionalOnClass(SphU.class)
@RequiredArgsConstructor
@Slf4j
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Configuration(proxyBeanMethods = false)
public class SentinelHotKeyAutoConfiguration implements InitializingBean {

	private static final String AUTOWIRED_SENTINEL_KEY = "Autowired SentinelHotKey";

	private final CacheProperties cacheProperties;

	@Override
	public void afterPropertiesSet() {
		log.debug(AUTOWIRED_SENTINEL_KEY);
		SentinelHotKeyClient.start(cacheProperties);
	}
}
