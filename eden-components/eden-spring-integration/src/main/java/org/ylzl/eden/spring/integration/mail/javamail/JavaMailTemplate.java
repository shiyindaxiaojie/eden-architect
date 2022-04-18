package org.ylzl.eden.spring.integration.mail.javamail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.ylzl.eden.spring.integration.mail.core.MailTemplate;
import org.ylzl.eden.spring.integration.mail.core.SendMailException;
import org.ylzl.eden.spring.integration.mail.core.SendMailRequest;
import org.ylzl.eden.spring.integration.mail.core.SendMailResponse;

/**
 * JavaMail 邮件操作模板
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@RequiredArgsConstructor
@Slf4j
public class JavaMailTemplate implements MailTemplate {

	private final JavaMailSender javaMailSender;

	/**
	 * 发送邮件
	 *
	 * @param sendMailRequest 发送邮件请求
	 * @return 发送邮件响应
	 */
	@Override
	public SendMailResponse send(SendMailRequest sendMailRequest) {
		log.debug("发送 JavaMail 邮件请求，参数：{}", sendMailRequest);
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(sendMailRequest.getFrom());
		message.setReplyTo(sendMailRequest.getReplyTo());
		message.setTo(sendMailRequest.getTo());
		message.setCc(sendMailRequest.getCc());
		message.setBcc(sendMailRequest.getBcc());
		message.setSentDate(sendMailRequest.getSentDate());
		message.setSubject(sendMailRequest.getSubject());
		message.setText(sendMailRequest.getText());
		message.setCc(sendMailRequest.getCc());
		try {
			javaMailSender.send(message);
			return SendMailResponse.builder().success(true).build();
		} catch (MailException e) {
			log.error("发送 JavaMail 邮件失败，异常：{}", e.getMessage(), e);
			throw new SendMailException(e.getMessage());
		}
	}
}
