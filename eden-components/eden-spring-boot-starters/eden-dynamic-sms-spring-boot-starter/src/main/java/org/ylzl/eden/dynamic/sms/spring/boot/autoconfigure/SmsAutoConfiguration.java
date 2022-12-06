package org.ylzl.eden.dynamic.sms.spring.boot.autoconfigure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.dynamic.sms.core.SmsTemplate;
import org.ylzl.eden.dynamic.sms.spring.boot.env.SmsProperties;
import org.ylzl.eden.dynamic.sms.spring.boot.support.SmsHelper;
import org.ylzl.eden.spring.boot.bootstrap.constant.Conditions;

/**
 * 短信操作模板策略自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ConditionalOnProperty(
	prefix = SmsProperties.PREFIX,
	name = Conditions.ENABLED,
	havingValue = Conditions.ENABLED_TRUE
)
@ConditionalOnBean(SmsTemplate.class)
@EnableConfigurationProperties(SmsProperties.class)
@RequiredArgsConstructor
@Slf4j
@Configuration(proxyBeanMethods = false)
public class SmsAutoConfiguration {

	public static final String AUTOWIRED_SMS_BEAN_FACTORY = "Autowired SmsBeanFactory";

	private final SmsProperties smsProperties;

	@Bean
	public SmsHelper smsHelper() {
		log.debug(AUTOWIRED_SMS_BEAN_FACTORY);
		return new SmsHelper(smsProperties.getPrimary());
	}
}
