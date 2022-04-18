package org.ylzl.eden.spring.integration.sms.core;

import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 发送模板短信请求
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
public class SendTemplateSmsRequest implements Serializable {

	/**
	 * 短信签名
	 */
	private String signName;

	/**
	 * 模板短信
	 */
	private TemplateSms templateSms;



	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	@EqualsAndHashCode
	@ToString
	@Data
	public static class TemplateSms {

		/**
		 * 手机号码
		 */
		private List<String> phoneNumbers;

		/**
		 * 短信模板编号
		 */
		private String templateCode;

		/**
		 * 短信模板参数
		 */
		private Map<String, String> templateParam;
	}
}
