package org.ylzl.eden.dynamic.mail.spring.boot.autoconfigure;

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
import org.ylzl.eden.dynamic.mail.MailTemplate;
import org.ylzl.eden.dynamic.mail.integration.javamail.JavaMailTemplate;
import org.ylzl.eden.dynamic.mail.spring.boot.env.MailProperties;
import org.ylzl.eden.dynamic.mail.spring.boot.support.MailBeanNames;
import org.ylzl.eden.spring.boot.bootstrap.constant.Conditions;

/**
 * JavaMailTemplate 自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ConditionalOnProperty(
	prefix = MailProperties.JavaMail.PREFIX,
	name = Conditions.ENABLED,
	havingValue = Conditions.TRUE,
	matchIfMissing = true
)
@Conditional(JavaMailAutoConfiguration.MailSenderCondition.class)
@AutoConfigureAfter(MailSenderAutoConfiguration.class)
@RequiredArgsConstructor
@Slf4j
@Configuration(proxyBeanMethods = false)
public class JavaMailAutoConfiguration {

	public static final String AUTOWIRED_JAVA_MAIL_TEMPLATE = "Autowired JavaMailTemplate";

	private final JavaMailSender javaMailSender;

	@Bean(MailBeanNames.JAVA_MAIL_TEMPLATE)
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
