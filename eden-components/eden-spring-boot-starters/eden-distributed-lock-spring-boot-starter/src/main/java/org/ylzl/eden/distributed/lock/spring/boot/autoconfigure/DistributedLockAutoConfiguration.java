package org.ylzl.eden.distributed.lock.spring.boot.autoconfigure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.distributed.lock.core.DistributedLock;
import org.ylzl.eden.distributed.lock.spring.boot.support.DistributedLockHelper;
import org.ylzl.eden.distributed.lock.spring.boot.env.DistributedLockProperties;

/**
 * 分布式锁操作自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ConditionalOnProperty(DistributedLockProperties.PREFIX)
@ConditionalOnBean(DistributedLock.class)
@EnableConfigurationProperties(DistributedLockProperties.class)
@RequiredArgsConstructor
@Slf4j
@Configuration(proxyBeanMethods = false)
public class DistributedLockAutoConfiguration {

	public static final String AUTOWIRED_DISTRIBUTED_LOCK_FACTORY = "Autowired DistributedLockFactory";

	private final DistributedLockProperties distributedLockProperties;

	@Bean
	public DistributedLockHelper distributedLockHelper() {
		log.debug(AUTOWIRED_DISTRIBUTED_LOCK_FACTORY);
		return new DistributedLockHelper(distributedLockProperties.getPrimary());
	}
}
