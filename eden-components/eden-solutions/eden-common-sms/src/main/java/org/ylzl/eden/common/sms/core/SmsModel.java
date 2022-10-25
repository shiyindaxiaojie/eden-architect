package org.ylzl.eden.common.sms.core;

import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * 短信模型
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
public class SmsModel {

	/**
	 * 手机号码
	 */
	private String phoneNumber;

	/**
	 * 短信内容
	 */
	private String smsContent;

	/**
	 * 自定义短信ID
	 */
	private String customSmsId;

	/**
	 * 扩展代码
	 */
	private String extendedCode;

	/**
	 * 定时发送时间（值为空表示立即发送）
	 */
	private String timerTime;
}
