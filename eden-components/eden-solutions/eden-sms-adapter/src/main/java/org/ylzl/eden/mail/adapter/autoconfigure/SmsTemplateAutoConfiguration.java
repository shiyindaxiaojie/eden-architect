package org.ylzl.eden.mail.adapter.autoconfigure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.mail.adapter.core.SmsTemplate;
import org.ylzl.eden.mail.adapter.core.SmsTemplateFactory;
import org.ylzl.eden.mail.adapter.env.SmsProperties;

/**
 * 短信操作模板策略自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ConditionalOnProperty(SmsProperties.PREFIX)
@ConditionalOnBean(SmsTemplate.class)
@EnableConfigurationProperties(SmsProperties.class)
@RequiredArgsConstructor
@Slf4j
@Configuration(proxyBeanMethods = false)
public class SmsTemplateAutoConfiguration {

	public static final String AUTOWIRED_SMS_TEMPLATE_FACTORY = "Autowired SmsTemplateFactory";

	private final SmsProperties smsProperties;

	@Bean
	public SmsTemplateFactory smsTemplateFactory() {
		log.debug(AUTOWIRED_SMS_TEMPLATE_FACTORY);
		return new SmsTemplateFactory(smsProperties.getType());
	}
}
