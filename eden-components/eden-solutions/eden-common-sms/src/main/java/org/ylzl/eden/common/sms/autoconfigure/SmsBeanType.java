package org.ylzl.eden.common.sms.autoconfigure;

import lombok.Getter;

/**
 * 短信注册类型
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Getter
public enum SmsBeanType {

	ALIYUN(SmsBeanType.ALIYUN_SMS_TEMPLATE),
	QCLOUD(SmsBeanType.QCLOUD_SMS_TEMPLATE),
	EMAY(SmsBeanType.EMAY_SMS_TEMPLATE),
	MONTNETS(SmsBeanType.MONTNETS_SMS_TEMPLATE);

	public static final String ALIYUN_SMS_TEMPLATE = "aliyunSmsTemplate";

	public static final String QCLOUD_SMS_TEMPLATE = "qcloudSmsTemplate";

	public static final String EMAY_SMS_TEMPLATE = "emaySmsTemplate";

	public static final String MONTNETS_SMS_TEMPLATE = "montnetsSmsTemplate";

	private final String templateName;

	SmsBeanType(String templateName) {
		this.templateName = templateName;
	}

	public static SmsBeanType parse(String type) {
		for (SmsBeanType smsBeanType : SmsBeanType.values()) {
			if (smsBeanType.name().equalsIgnoreCase(type)) {
				return smsBeanType;
			}
		}
		return null;
	}
}
