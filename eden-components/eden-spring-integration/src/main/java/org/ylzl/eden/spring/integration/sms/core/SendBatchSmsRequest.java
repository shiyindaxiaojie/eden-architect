package org.ylzl.eden.spring.integration.sms.core;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * 相同内容群发短信请求
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
public class SendBatchSmsRequest implements Serializable {

	/**
	 * 短信签名
	 */
	private String signName;

	/**
	 * 手机号码
	 */
	private List<String> phoneNumbers;

	/**
	 * 短信内容
	 */
	private String smsContent;

	/**
	 * 自定义ID
	 */
	private String customId;
}
