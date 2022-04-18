package org.ylzl.eden.spring.integration.sms.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.spring.integration.sms.core.SmsTemplate;
import org.ylzl.eden.spring.integration.sms.core.SmsTemplateStrategy;

import java.util.stream.Collectors;

/**
 * 短信操作模板策略自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@ConditionalOnBean(SmsTemplate.class)
@Slf4j
@Configuration
public class SmsTemplateStrategyAutoConfiguration {

	@Bean
	public SmsTemplateStrategy smsTemplateStrategy(ObjectProvider<SmsTemplate> strategies) {
		return new SmsTemplateStrategy(strategies.stream().collect(Collectors.toList()));
	}
}
