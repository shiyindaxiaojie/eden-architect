package org.ylzl.eden.distributed.uid.autoconfigure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.distributed.uid.core.DistributedUID;
import org.ylzl.eden.distributed.uid.core.DistributedUIDFactory;
import org.ylzl.eden.distributed.uid.env.DistributedUIDProperties;

/**
 * 分布式唯一ID操作自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ConditionalOnProperty(DistributedUIDProperties.PREFIX)
@ConditionalOnBean(DistributedUID.class)
@EnableConfigurationProperties(DistributedUIDProperties.class)
@RequiredArgsConstructor
@Slf4j
@Configuration(proxyBeanMethods = false)
public class DistributedUIDAutoConfiguration {

	public static final String AUTOWIRED_DISTRIBUTED_UID_FACTORY = "Autowired DistributedUIDFactory";

	private final DistributedUIDProperties distributedUIDProperties;

	@Bean
	public DistributedUIDFactory distributedUIDFactory() {
		log.debug(AUTOWIRED_DISTRIBUTED_UID_FACTORY);
		return new DistributedUIDFactory(distributedUIDProperties.getType());
	}
}
