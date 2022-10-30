package org.ylzl.eden.common.mail.core;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Date;

/**
 * 邮件模型
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
@Data
public class MailModel {

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
