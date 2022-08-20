package org.ylzl.eden.spring.integration.sms.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.spring.integration.sms.core.SmsTemplate;
import org.ylzl.eden.spring.integration.sms.core.SmsTemplateStrategy;

/**
 * 短信操作模板策略自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ConditionalOnBean(SmsTemplate.class)
@Slf4j
@Configuration(proxyBeanMethods = false)
public class SmsTemplateStrategyAutoConfiguration {

	public static final String AUTOWIRED_SMS_TEMPLATE_STRATEGY = "Autowired SmsTemplateStrategy";

	@Bean
	public SmsTemplateStrategy smsTemplateStrategy() {
		log.debug(AUTOWIRED_SMS_TEMPLATE_STRATEGY);
		return new SmsTemplateStrategy();
	}
}
