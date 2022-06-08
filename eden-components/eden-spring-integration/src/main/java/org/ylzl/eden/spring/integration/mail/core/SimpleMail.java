package org.ylzl.eden.spring.integration.mail.core;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Date;

/**
 * 简单邮件
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
@Data
public class SimpleMail {

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
