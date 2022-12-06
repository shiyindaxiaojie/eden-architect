package org.ylzl.eden.dynamic.sms.model.single;

import lombok.*;

import java.io.Serializable;

/**
 * 发送单条短信响应
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
public class SingleSendSmsResponse implements Serializable {

	/**
	 * 短信ID
	 */
	private String smsId;

	/**
	 * 自定义短信ID
	 */
	private String customSmsId;

	/**
	 * 处理成功
	 */
	private boolean success;

	/**
	 * 错误码
	 */
	private String errCode;

	/**
	 * 错误描述
	 */
	private String errMessage;
}
