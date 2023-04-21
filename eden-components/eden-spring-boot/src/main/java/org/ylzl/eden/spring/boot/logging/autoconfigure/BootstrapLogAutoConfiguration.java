package org.ylzl.eden.spring.boot.logging.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.ylzl.eden.spring.boot.bootstrap.constant.Conditions;
import org.ylzl.eden.spring.boot.logging.env.BootstrapLogProperties;
import org.ylzl.eden.spring.framework.logging.EnableBootstrapLog;

/**
 * 全局日志自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ConditionalOnProperty(
	prefix = BootstrapLogProperties.PREFIX,
	name = Conditions.ENABLED,
	havingValue = Conditions.TRUE,
	matchIfMissing = true
)
@EnableConfigurationProperties(BootstrapLogProperties.class)
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Slf4j
@Configuration(proxyBeanMethods = false)
@EnableBootstrapLog
public class BootstrapLogAutoConfiguration {

}
