package org.ylzl.eden.distributed.lock.integration.redisson.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.distributed.lock.autoconfigure.DistributedLockAutoConfiguration;
import org.ylzl.eden.distributed.lock.core.DistributedLock;
import org.ylzl.eden.distributed.lock.core.DistributedLockFactory;
import org.ylzl.eden.distributed.lock.env.DistributedLockProperties;
import org.ylzl.eden.distributed.lock.integration.redisson.core.RedissonDistributedLock;

/**
 * Redisson 分布式锁自动配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@AutoConfigureBefore(DistributedLockAutoConfiguration.class)
@AutoConfigureAfter(RedissonAutoConfiguration.class)
@ConditionalOnProperty(value = RedissonDistributedLockAutoConfiguration.ENABLED, matchIfMissing = true)
@ConditionalOnClass(RedissonClient.class)
@Slf4j
@Configuration(proxyBeanMethods = false)
public class RedissonDistributedLockAutoConfiguration {

	public static final String ENABLED = DistributedLockProperties.PREFIX + ".curator.enabled";

	public static final String TYPE = "REDISSON";

	public static final String BEAN = "redissonDistributedLock";

	public static final String AUTOWIRED_REDISSON_DISTRIBUTED_LOCK = "Autowired RedissonDistributedLock";

	@ConditionalOnClass(Redisson.class)
	@ConditionalOnBean(RedissonClient.class)
	@Bean(BEAN)
	public DistributedLock distributedLock(RedissonClient redissonClient) {
		log.debug(AUTOWIRED_REDISSON_DISTRIBUTED_LOCK);
		DistributedLockFactory.addBean(TYPE, BEAN);
		return new RedissonDistributedLock(redissonClient);
	}
}
