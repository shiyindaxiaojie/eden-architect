package org.ylzl.eden.spring.boot.cat.autoconfigure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.rpc.Filter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.spring.cloud.cat.integration.dubbo.EnableCatDubbo;

/**
 * Dubbo 集成 CAT 自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ConditionalOnClass(Filter.class)
@ConditionalOnBean(CatAutoConfiguration.class)
@ConditionalOnProperty(prefix = "dubbo", name = "enabled", matchIfMissing = true)
@RequiredArgsConstructor
@Slf4j
@Configuration(proxyBeanMethods = false)
public class DubboCatAutoConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() {
		EnableCatDubbo.enable();
	}
}
