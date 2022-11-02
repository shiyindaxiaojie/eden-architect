package org.ylzl.eden.distributed.lock.integration.zookeeper.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.distributed.lock.autoconfigure.DistributedLockAutoConfiguration;
import org.ylzl.eden.distributed.lock.integration.zookeeper.core.ZookeeperDistributedLock;
import org.ylzl.eden.distributed.lock.core.DistributedLock;
import org.ylzl.eden.distributed.lock.core.DistributedLockFactory;
import org.ylzl.eden.distributed.lock.env.DistributedLockProperties;
import org.ylzl.eden.zookeeper.spring.cloud.core.ZookeeperTemplate;

/**
 * Curator 分布式锁自动配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@AutoConfigureBefore(DistributedLockAutoConfiguration.class)
@ConditionalOnProperty(value = ZookeeperDistributedLockAutoConfiguration.ENABLED, matchIfMissing = false)
@ConditionalOnClass(ZooKeeper.class)
@Slf4j
@Configuration(proxyBeanMethods = false)
@Deprecated
public class ZookeeperDistributedLockAutoConfiguration {

	public static final String ENABLED = DistributedLockProperties.PREFIX + ".zookeeper.enabled";

	public static final String TYPE = "ZOOKEEPER";

	public static final String BEAN = "zookeeperDistributedLock";

	public static final String AUTOWIRED_ZOOKEEPER_DISTRIBUTED_LOCK = "Autowired ZookeeperDistributedLock";

	@ConditionalOnBean(ZookeeperTemplate.class)
	@Bean(BEAN)
	public DistributedLock distributedLock(ZookeeperTemplate zookeeperTemplate) {
		log.debug(AUTOWIRED_ZOOKEEPER_DISTRIBUTED_LOCK);
		DistributedLockFactory.addBean(TYPE, BEAN);
		return new ZookeeperDistributedLock(zookeeperTemplate);
	}
}
