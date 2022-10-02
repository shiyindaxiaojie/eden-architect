package org.ylzl.eden.mq.adapter.core;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * 消息模型
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
public class Message {

	/**
	 * 命名空间
	 */
	private String namespace;

	/**
	 * 主题
	 */
	private String topic;

	/**
	 * 分区/队列
	 */
	private Integer partition;

	/**
	 * 分区键
	 */
	private String key;

	/**
	 * 标签过滤
	 */
	private String tags;

	/**
	 * 消息体
	 */
	private String body;

	/**
	 * 延时等级
	 */
	@Builder.Default
	private Integer delayTimeLevel = 0;
}
