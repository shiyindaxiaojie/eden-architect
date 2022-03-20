package org.ylzl.eden.spring.integration.messagequeue.producer;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * 消息模型
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Data
public class Message {

	private String topic;

	private String key;

	private String tags;

	private String body;

	@Builder.Default
	private Integer delayTimeLevel = 0;

	private String transactionId;
}
