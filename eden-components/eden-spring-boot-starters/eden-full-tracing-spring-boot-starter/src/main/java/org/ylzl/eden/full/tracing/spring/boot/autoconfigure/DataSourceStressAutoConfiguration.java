package org.ylzl.eden.full.tracing.spring.boot.autoconfigure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.full.tracing.integration.jdbc.DataSourceShadowAspect;
import org.ylzl.eden.full.tracing.spring.boot.env.DataSourceShadowProperties;

/**
 * 数据源压测标记自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@ConditionalOnProperty(value = DataSourceStressAutoConfiguration.ENABLED, havingValue = "true")
@EnableConfigurationProperties(DataSourceShadowProperties.class)
@RequiredArgsConstructor
@Slf4j
@Configuration(proxyBeanMethods = false)
public class DataSourceStressAutoConfiguration {

	public static final String ENABLED ="stress.jdbc.enabled";

	@Bean
	public DataSourceShadowAspect dataSourceShadowAspect(DataSourceShadowProperties dataSourceShadowProperties) {
		return new DataSourceShadowAspect(dataSourceShadowProperties.getName());
	}
}
