package org.ylzl.eden.mail.adapter.core;

import com.google.common.collect.Lists;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * 个性化群发短信请求
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Data
public class MultiSendSmsRequest implements Serializable {

	/**
	 * 个性化群发短信
	 */
	private List<SendSms> sendSmsList = Lists.newArrayList();

	/**
	 * 添加群发对象
	 *
	 * @param sendSms
	 */
	public void addSendSms(SendSms sendSms) {
		sendSmsList.add(sendSms);
	}
}
