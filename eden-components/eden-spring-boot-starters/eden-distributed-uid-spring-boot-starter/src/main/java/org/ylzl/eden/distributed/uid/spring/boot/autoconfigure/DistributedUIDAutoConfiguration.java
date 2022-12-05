package org.ylzl.eden.distributed.uid.spring.boot.autoconfigure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.distributed.uid.core.DistributedUID;
import org.ylzl.eden.distributed.uid.spring.boot.env.DistributedUIDProperties;
import org.ylzl.eden.distributed.uid.spring.boot.support.DistributedUIDHelper;
import org.ylzl.eden.spring.boot.bootstrap.constant.ConditionConstants;

/**
 * 分布式唯一ID操作自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ConditionalOnProperty(
	prefix = DistributedUIDProperties.PREFIX,
	name = ConditionConstants.ENABLED,
	havingValue = ConditionConstants.ENABLED_TRUE
)
@ConditionalOnBean(DistributedUID.class)
@EnableConfigurationProperties(DistributedUIDProperties.class)
@RequiredArgsConstructor
@Slf4j
@Configuration(proxyBeanMethods = false)
public class DistributedUIDAutoConfiguration {

	public static final String AUTOWIRED_DISTRIBUTED_UID_HELPER = "Autowired DistributedUIDHelper";

	private final DistributedUIDProperties distributedUIDProperties;

	@Bean
	public DistributedUIDHelper distributedUIDHelper() {
		log.debug(AUTOWIRED_DISTRIBUTED_UID_HELPER);
		return new DistributedUIDHelper(distributedUIDProperties.getPrimary());
	}
}
