package org.ylzl.eden.distributed.lock.spring.boot.autoconfigure;

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
import org.ylzl.eden.distributed.lock.core.DistributedLock;
import org.ylzl.eden.distributed.lock.integration.redisson.RedissonDistributedLock;
import org.ylzl.eden.distributed.lock.spring.boot.support.DistributedLockBeanNames;
import org.ylzl.eden.distributed.lock.spring.boot.env.DistributedLockProperties;
import org.ylzl.eden.spring.boot.bootstrap.constant.Conditions;

/**
 * Redisson 分布式锁自动配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ConditionalOnProperty(
	prefix = DistributedLockProperties.Redisson.PREFIX,
	name = Conditions.ENABLED,
	havingValue = Conditions.ENABLED_TRUE,
	matchIfMissing = true
)
@AutoConfigureBefore(DistributedLockAutoConfiguration.class)
@AutoConfigureAfter(RedissonAutoConfiguration.class)
@ConditionalOnClass(RedissonClient.class)
@Slf4j
@Configuration(proxyBeanMethods = false)
public class RedissonDistributedLockAutoConfiguration {

	public static final String AUTOWIRED_REDISSON_DISTRIBUTED_LOCK = "Autowired RedissonDistributedLock";

	@ConditionalOnClass(Redisson.class)
	@ConditionalOnBean(RedissonClient.class)
	@Bean(DistributedLockBeanNames.REDISSON_DISTRIBUTED_LOCK)
	public DistributedLock distributedLock(RedissonClient redissonClient) {
		log.debug(AUTOWIRED_REDISSON_DISTRIBUTED_LOCK);
		return new RedissonDistributedLock(redissonClient);
	}
}
