package org.ylzl.eden.spring.integration.sms.core;

import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 个性化群发短信请求
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Data
public class SendMultiSmsRequest implements Serializable {

	/**
	 * 短信签名
	 */
	private String signName;

	/**
	 * 个性化群发短信
	 */
	private List<MultiSms> multiSmsList = new ArrayList<>();

	/**
	 * 添加群发对象
	 *
	 * @param multiSms
	 */
	public void addMixedSms(MultiSms multiSms) {
		multiSmsList.add(multiSms);
	}

	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	@EqualsAndHashCode
	@ToString
	@Data
	public static class MultiSms {

		/**
		 * 手机号码
		 */
		private String phoneNumber;

		/**
		 * 短信内容
		 */
		private String smsContent;

		/**
		 * 自定义ID
		 */
		private String customId;
	}
}
