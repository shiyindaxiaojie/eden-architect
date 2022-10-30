package org.ylzl.eden.common.mail.core.multi;

import com.google.common.collect.Lists;
import lombok.*;
import org.ylzl.eden.common.mail.core.MailModel;

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
	private List<MailModel> mailModelList = Lists.newArrayList();

	/**
	 * 添加群发对象
	 *
	 * @param mailModel
	 */
	public void addSimpleMail(MailModel mailModel) {
		mailModelList.add(mailModel);
	}
}
