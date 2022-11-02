package org.ylzl.eden.common.sms.spring.boot.autoconfigure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.common.sms.SmsTemplate;
import org.ylzl.eden.common.sms.spring.boot.env.SmsProperties;

/**
 * 短信操作模板策略自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ConditionalOnProperty(name = SmsProperties.ENABLED, havingValue = "true", matchIfMissing = true)
@ConditionalOnBean(SmsTemplate.class)
@EnableConfigurationProperties(SmsProperties.class)
@RequiredArgsConstructor
@Slf4j
@Configuration(proxyBeanMethods = false)
public class SmsAutoConfiguration {

	public static final String AUTOWIRED_SMS_BEAN_FACTORY = "Autowired SmsBeanFactory";

	private final SmsProperties smsProperties;

	@Bean
	public SmsBeanFactory smsBeanFactory() {
		log.debug(AUTOWIRED_SMS_BEAN_FACTORY);
		return new SmsBeanFactory(smsProperties.getType());
	}
}
