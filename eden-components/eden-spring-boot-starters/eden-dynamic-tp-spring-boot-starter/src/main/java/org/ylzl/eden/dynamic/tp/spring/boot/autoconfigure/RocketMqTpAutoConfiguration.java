package org.ylzl.eden.dynamic.tp.spring.boot.autoconfigure;

import com.dtp.adapter.rocketmq.RocketMqDtpAdapter;
import com.dtp.starter.common.autoconfigure.BaseBeanAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.ylzl.eden.dynamic.mq.integration.rocketmq.RocketMQConsumer;
import org.ylzl.eden.dynamic.tp.spring.boot.adapter.CustomRocketMqDtpAdapter;
import org.ylzl.eden.spring.boot.bootstrap.constant.Conditions;

/**
 * 自定义 RocketMqDtpAdapter 装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@ConditionalOnProperty(
    prefix = "spring.dynamic.tp",
    name = Conditions.ENABLED,
    havingValue = Conditions.TRUE,
    matchIfMissing = true
)
@ConditionalOnBean(RocketMQConsumer.class)
@AutoConfigureAfter({BaseBeanAutoConfiguration.class})
@Slf4j
@Configuration(proxyBeanMethods = false)
public class RocketMqTpAutoConfiguration {

	private static final String AUTOWIRED_CUSTOM_ROCKET_MQ_DTP_ADAPTER = "Autowired CustomRocketMqDtpAdapter";

	@Primary
    @Bean
    public RocketMqDtpAdapter rocketMqDtpHandler() {
        log.debug(AUTOWIRED_CUSTOM_ROCKET_MQ_DTP_ADAPTER);
		return new CustomRocketMqDtpAdapter();
    }
}
