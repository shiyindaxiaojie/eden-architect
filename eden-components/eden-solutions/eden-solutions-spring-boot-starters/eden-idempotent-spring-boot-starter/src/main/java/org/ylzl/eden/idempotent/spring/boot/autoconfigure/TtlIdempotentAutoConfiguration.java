package org.ylzl.eden.idempotent.spring.boot.autoconfigure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.ylzl.eden.idempotent.spring.boot.env.IdempotentTtlProperties;

/**
 * 幂等请求自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ConditionalOnProperty(IdempotentTtlProperties.PREFIX)
@EnableConfigurationProperties(IdempotentTtlProperties.class)
@RequiredArgsConstructor
@Import(IdempotentAspectRegistrar.class)
@Slf4j
@Configuration(proxyBeanMethods = false)
public class TtlIdempotentAutoConfiguration {


}
