package org.ylzl.spring.boot.redisson.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.distributed.lock.core.DistributedLock;
import org.ylzl.eden.distributed.lock.redisson.RedissonDistributedLock;

/**
 * Redisson 分布式锁自动配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ConditionalOnClass(RedissonClient.class)
@AutoConfigureAfter(RedissonAutoConfiguration.class)
@ConditionalOnProperty(value = "distributed-lock.redisson.enabled", matchIfMissing = true)
@Slf4j
@Configuration(proxyBeanMethods = false)
public class RedissonDistributedLockAutoConfiguration {

	@ConditionalOnClass(Redisson.class)
	@ConditionalOnBean(RedissonClient.class)
	@Bean("redissonDistributedLock")
	public DistributedLock distributedLock(RedissonClient redissonClient) {
		return new RedissonDistributedLock(redissonClient);
	}
}
