package org.ylzl.eden.common.mq.core.producer;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * 消息发送结果
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Data
public class MessageSendResult {

	/**
	 * 主题
	 */
	private String topic;

	/**
	 * 分区
	 */
	private Integer partition;

	/**
	 * 偏移量
	 */
	private Long offset;

	/**
	 * 事务ID
	 */
	private String transactionId;
}
