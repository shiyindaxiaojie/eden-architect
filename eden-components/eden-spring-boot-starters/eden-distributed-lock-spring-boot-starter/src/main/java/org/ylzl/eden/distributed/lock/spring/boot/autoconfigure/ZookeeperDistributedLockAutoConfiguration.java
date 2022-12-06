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
import org.ylzl.eden.distributed.lock.spring.boot.env.DistributedLockProperties;
import org.ylzl.eden.distributed.lock.spring.boot.support.DistributedLockBeanNames;
import org.ylzl.eden.spring.boot.bootstrap.constant.Conditions;
import org.ylzl.eden.spring.cloud.zookeeper.core.ZookeeperTemplate;

/**
 * Curator 分布式锁自动配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ConditionalOnProperty(
	prefix = DistributedLockProperties.ZooKeeper.PREFIX,
	name = Conditions.ENABLED,
	havingValue = Conditions.ENABLED_TRUE
)
@AutoConfigureBefore(DistributedLockAutoConfiguration.class)
@ConditionalOnClass(ZooKeeper.class)
@Slf4j
@Configuration(proxyBeanMethods = false)
public class ZookeeperDistributedLockAutoConfiguration {

	public static final String AUTOWIRED_ZOOKEEPER_DISTRIBUTED_LOCK = "Autowired ZookeeperDistributedLock";

	@ConditionalOnBean(ZookeeperTemplate.class)
	@Bean(DistributedLockBeanNames.ZOOKEEPER_DISTRIBUTED_LOCK)
	public DistributedLock distributedLock(ZookeeperTemplate zookeeperTemplate) {
		log.debug(AUTOWIRED_ZOOKEEPER_DISTRIBUTED_LOCK);
		return new ZookeeperDistributedLock(zookeeperTemplate.getZookeeper());
	}
}
