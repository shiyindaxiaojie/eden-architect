package org.ylzl.eden.common.sms.core;

import lombok.Getter;

/**
 * 短信类型
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Getter
public enum SmsType {

	DEFAULT(null),
	ALIYUN(SmsType.ALIYUN_SMS_TEMPLATE),
	QCLOUD(SmsType.QCLOUD_SMS_TEMPLATE),
	EMAY(SmsType.EMAY_SMS_TEMPLATE),
	MONTNETS(SmsType.MONTNETS_SMS_TEMPLATE);

	public static final String ALIYUN_SMS_TEMPLATE = "aliyunSmsTemplate";

	public static final String QCLOUD_SMS_TEMPLATE = "qcloudSmsTemplate";

	public static final String EMAY_SMS_TEMPLATE = "emaySmsTemplate";

	public static final String MONTNETS_SMS_TEMPLATE = "montnetsSmsTemplate";

	private final String templateName;

	SmsType(String templateName) {
		this.templateName = templateName;
	}

	public static SmsType parse(String type) {
		for (SmsType smsType : SmsType.values()) {
			if (smsType.name().equalsIgnoreCase(type)) {
				return smsType;
			}
		}
		return null;
	}
}
