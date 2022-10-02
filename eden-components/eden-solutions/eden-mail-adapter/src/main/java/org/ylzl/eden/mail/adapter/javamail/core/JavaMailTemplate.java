package org.ylzl.eden.mail.adapter.javamail.core;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.ylzl.eden.mail.adapter.core.*;

/**
 * JavaMail 邮件操作模板
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
@Slf4j
public class JavaMailTemplate implements MailTemplate {

	private final JavaMailSender javaMailSender;

	/**
	 * 单条发送邮件
	 *
	 * @param request 发送邮件请求
	 * @return 发送邮件响应
	 */
	@Override
	public SingleSendMailResponse singleSend(SingleSendMailRequest request) {
		log.debug("单条发送 JavaMail 邮件请求，参数：{}", request);
		SimpleMailMessage message = buildSimpleMailMessage(request);
		try {
			javaMailSender.send(message);
			return SingleSendMailResponse.builder().success(true).build();
		} catch (MailException e) {
			log.error("单条发送 JavaMail 邮件失败，异常：{}", e.getMessage(), e);
			throw new SendMailException(e.getMessage());
		}
	}

	/**
	 * 批量发送个性化邮件
	 *
	 * @param request 发送邮件请求
	 * @return 发送邮件响应
	 */
	@Override
	public MultiSendMailResponse multiSend(MultiSendMailRequest request) {
		log.debug("批量发送 JavaMail 个性化邮件请求，参数：{}", request);
		SimpleMailMessage[] simpleMailMessages =
			request.getSimpleMailList().stream()
				.map(this::buildSimpleMailMessage).toArray(SimpleMailMessage[]::new);
		try {
			javaMailSender.send(simpleMailMessages);
			return MultiSendMailResponse.builder().success(true).build();
		} catch (MailException e) {
			log.error("批量发送 JavaMail 个性化邮件失败，异常：{}", e.getMessage(), e);
			throw new SendMailException(e.getMessage());
		}
	}

	private SimpleMailMessage buildSimpleMailMessage(SimpleMail request) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(request.getFrom());
		message.setReplyTo(request.getReplyTo());
		message.setTo(request.getTo());
		message.setCc(request.getCc());
		message.setBcc(request.getBcc());
		message.setSentDate(request.getSentDate());
		message.setSubject(request.getSubject());
		message.setText(request.getText());
		message.setCc(request.getCc());
		return message;
	}
}
