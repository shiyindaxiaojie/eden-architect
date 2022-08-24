package org.ylzl.eden.spring.boot.curator.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.distributed.lock.core.DistributedLock;
import org.ylzl.eden.distributed.lock.curator.CuratorDistributedLock;

/**
 * Curator 自动配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ConditionalOnProperty(value = "distributed-lock.curator.enabled", matchIfMissing = true)
@Slf4j
@Configuration(proxyBeanMethods = false)
public class CuratorDistributedLockAutoConfiguration {

	@ConditionalOnClass(CuratorFramework.class)
	@ConditionalOnBean(CuratorFramework.class)
	@Bean
	public DistributedLock distributedLock(CuratorFramework curatorFramework) {
		return new CuratorDistributedLock(curatorFramework);
	}
}
