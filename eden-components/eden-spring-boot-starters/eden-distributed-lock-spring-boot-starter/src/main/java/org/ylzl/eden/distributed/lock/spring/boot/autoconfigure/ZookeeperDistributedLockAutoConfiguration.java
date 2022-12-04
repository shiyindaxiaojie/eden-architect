package org.ylzl.eden.distributed.lock.spring.boot.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.distributed.lock.core.DistributedLock;
import org.ylzl.eden.distributed.lock.integration.zookeeper.ZookeeperDistributedLock;
import org.ylzl.eden.distributed.lock.spring.boot.support.DistributedLockBeanType;
import org.ylzl.eden.distributed.lock.spring.boot.env.DistributedLockProperties;
import org.ylzl.eden.spring.cloud.zookeeper.core.ZookeeperTemplate;

/**
 * Curator 分布式锁自动配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@AutoConfigureBefore(DistributedLockAutoConfiguration.class)
@ConditionalOnProperty(value = ZookeeperDistributedLockAutoConfiguration.ENABLED, havingValue = "true")
@ConditionalOnClass(ZooKeeper.class)
@Slf4j
@Configuration(proxyBeanMethods = false)
public class ZookeeperDistributedLockAutoConfiguration {

	public static final String ENABLED = DistributedLockProperties.PREFIX + ".zookeeper.enabled";

	public static final String AUTOWIRED_ZOOKEEPER_DISTRIBUTED_LOCK = "Autowired ZookeeperDistributedLock";

	@ConditionalOnBean(ZookeeperTemplate.class)
	@Bean(DistributedLockBeanType.ZOOKEEPER_DISTRIBUTED_LOCK)
	public DistributedLock distributedLock(ZookeeperTemplate zookeeperTemplate) {
		log.debug(AUTOWIRED_ZOOKEEPER_DISTRIBUTED_LOCK);
		return new ZookeeperDistributedLock(zookeeperTemplate);
	}
}
