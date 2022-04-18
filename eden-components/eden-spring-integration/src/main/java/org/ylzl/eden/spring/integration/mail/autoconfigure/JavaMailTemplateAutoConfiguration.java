package org.ylzl.eden.spring.integration.mail.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.AnyNestedCondition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.ylzl.eden.spring.integration.mail.core.MailTemplate;
import org.ylzl.eden.spring.integration.mail.javamail.JavaMailTemplate;

/**
 * JavaMailTemplate 自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@AutoConfigureAfter(MailSenderAutoConfiguration.class)
@Conditional(JavaMailTemplateAutoConfiguration.MailSenderCondition.class)
@Slf4j
@Configuration
public class JavaMailTemplateAutoConfiguration {

	private final JavaMailSender javaMailSender;

	public JavaMailTemplateAutoConfiguration(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

	@Bean("javaMailTemplate")
	public MailTemplate mailTemplate() {
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
