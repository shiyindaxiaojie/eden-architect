package org.ylzl.eden.distributed.lock.spring.boot.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.ylzl.eden.distributed.lock.core.DistributedLock;
import org.ylzl.eden.distributed.lock.integration.jedis.JedisDistributedLock;
import org.ylzl.eden.distributed.lock.spring.boot.support.DistributedLockBeanType;
import org.ylzl.eden.distributed.lock.spring.boot.env.DistributedLockProperties;
import redis.clients.jedis.Jedis;

/**
 * Jedis 分布式锁自动配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@AutoConfigureBefore(DistributedLockAutoConfiguration.class)
@ConditionalOnProperty(value = JedisDistributedLockAutoConfiguration.ENABLED, havingValue = "true")
@ConditionalOnClass(Jedis.class)
@Slf4j
@Configuration(proxyBeanMethods = false)
public class JedisDistributedLockAutoConfiguration {

	public static final String ENABLED = DistributedLockProperties.PREFIX + ".jedis.enabled";

	public static final String AUTOWIRED_JEDIS_DISTRIBUTED_LOCK = "Autowired JedisDistributedLock";

	@ConditionalOnClass(Jedis.class)
	@Bean(DistributedLockBeanType.JEDIS_DISTRIBUTED_LOCK)
	public DistributedLock distributedLock(RedisTemplate<String, Object> redisTemplate) {
		log.debug(AUTOWIRED_JEDIS_DISTRIBUTED_LOCK);
		return new JedisDistributedLock(redisTemplate);
	}
}
