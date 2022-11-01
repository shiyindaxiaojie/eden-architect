package org.ylzl.eden.common.mail.integration.javamail.autoconfigure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.AnyNestedCondition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.ylzl.eden.common.mail.core.MailTemplate;
import org.ylzl.eden.common.mail.autoconfigure.MailBeanType;
import org.ylzl.eden.common.mail.integration.javamail.core.JavaMailTemplate;

/**
 * JavaMailTemplate 自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@AutoConfigureAfter(MailSenderAutoConfiguration.class)
@Conditional(JavaMailTemplateAutoConfiguration.MailSenderCondition.class)
@RequiredArgsConstructor
@Slf4j
@Configuration(proxyBeanMethods = false)
public class JavaMailTemplateAutoConfiguration {

	public static final String AUTOWIRED_JAVA_MAIL_TEMPLATE = "Autowired JavaMailTemplate";

	private final JavaMailSender javaMailSender;

	@Bean(MailBeanType.JAVA_MAIL_TEMPLATE)
	public MailTemplate mailTemplate() {
		log.debug(AUTOWIRED_JAVA_MAIL_TEMPLATE);
		return new JavaMailTemplate(javaMailSender);
	}

	static class MailSenderCondition extends AnyNestedCondition {

		MailSenderCondition() {
			super(ConfigurationPhase.PARSE_CONFIGURATION);
		}

		@ConditionalOnProperty(prefix = "spring.mail", name = "host")
		static class HostProperty {}

		@ConditionalOnProperty(prefix = "spring.mail", name = "jndi-name")
		static class JndiNameProperty {}
	}
}
