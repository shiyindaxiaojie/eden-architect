package org.ylzl.eden.spring.integration.mail.core;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * 发送邮件请求
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
@Data
public class SendMailRequest implements Serializable {

	private String from;

	private String replyTo;

	private String[] to;

	private String[] cc;

	private String[] bcc;

	private Date sentDate;

	private String subject;

	private String text;

	private Boolean multipart;
}
