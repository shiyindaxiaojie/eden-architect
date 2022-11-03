package org.ylzl.eden.common.mail.spring.boot.autoconfigure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.common.mail.core.MailTemplate;
import org.ylzl.eden.common.mail.spring.boot.autoconfigure.factory.MailBeanFactory;
import org.ylzl.eden.common.mail.spring.boot.env.MailProperties;

/**
 * 短信操作模板策略自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ConditionalOnProperty(name = MailProperties.ENABLED, havingValue = "true", matchIfMissing = true)
@ConditionalOnBean(MailTemplate.class)
@EnableConfigurationProperties(MailProperties.class)
@RequiredArgsConstructor
@Slf4j
@Configuration(proxyBeanMethods = false)
public class MailAutoConfiguration {

	public static final String AUTOWIRED_MAIL_BEAN_FACTORY = "Autowired MailBeanFactory";

	private final MailProperties mailProperties;

	@Bean
	public MailBeanFactory mailBeanFactory() {
		log.debug(AUTOWIRED_MAIL_BEAN_FACTORY);
		return new MailBeanFactory(mailProperties.getType());
	}
}
