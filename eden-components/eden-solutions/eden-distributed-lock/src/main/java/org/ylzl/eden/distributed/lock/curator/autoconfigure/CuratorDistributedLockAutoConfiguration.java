package org.ylzl.eden.distributed.lock.curator.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.bouncycastle.asn1.anssi.ANSSINamedCurves;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.distributed.lock.autoconfigure.DistributedLockAutoConfiguration;
import org.ylzl.eden.distributed.lock.core.DistributedLock;
import org.ylzl.eden.distributed.lock.core.DistributedLockFactory;
import org.ylzl.eden.distributed.lock.curator.core.CuratorDistributedLock;
import org.ylzl.eden.distributed.lock.env.DistributedLockProperties;

/**
 * Curator 分布式锁自动配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@AutoConfigureBefore(DistributedLockAutoConfiguration.class)
@ConditionalOnProperty(value = CuratorDistributedLockAutoConfiguration.ENABLED, matchIfMissing = true)
@ConditionalOnClass(CuratorFramework.class)
@Slf4j
@Configuration(proxyBeanMethods = false)
public class CuratorDistributedLockAutoConfiguration {

	public static final String ENABLED = DistributedLockProperties.PREFIX + ".curator.enabled";

	public static final String TYPE = "CURATOR";

	public static final String BEAN = "curatorDistributedLock";

	public static final String AUTOWIRED_CURATOR_DISTRIBUTED_LOCK = "Autowired CuratorDistributedLock";

	@ConditionalOnBean(CuratorFramework.class)
	@Bean(BEAN)
	public DistributedLock distributedLock(CuratorFramework curatorFramework) {
		log.debug(AUTOWIRED_CURATOR_DISTRIBUTED_LOCK);
		DistributedLockFactory.addBean(TYPE, BEAN);
		return new CuratorDistributedLock(curatorFramework);
	}
}
