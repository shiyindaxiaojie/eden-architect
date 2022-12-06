package org.ylzl.eden.dynamic.mail.model.multi;

import com.google.common.collect.Lists;
import lombok.*;
import org.ylzl.eden.dynamic.mail.model.Mail;

import java.io.Serializable;
import java.util.List;

/**
 * 批量发送个性化邮件请求
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
@Data
public class MultiSendMailRequest implements Serializable {

	/**
	 * 个性化群发短信
	 */
	private List<Mail> mailList = Lists.newArrayList();

	/**
	 * 添加群发对象
	 *
	 * @param mail
	 */
	public void addSimpleMail(Mail mail) {
		mailList.add(mail);
	}
}
