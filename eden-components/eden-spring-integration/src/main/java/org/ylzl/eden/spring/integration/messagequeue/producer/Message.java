package org.ylzl.eden.spring.integration.messagequeue.producer;

import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * 消息模型
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@SuperBuilder
@ToString
public class Message {

	private String topic;

	private String key;

	private String tags;

	private String value;

	private Integer delayTimeLevel;
}
