/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ylzl.eden.common.mq;

import org.springframework.stereotype.Component;
import org.ylzl.eden.common.mq.consumer.ConsumeMode;
import org.ylzl.eden.common.mq.consumer.MessageModel;
import org.ylzl.eden.common.mq.consumer.MessageSelectorType;
import org.ylzl.eden.commons.lang.Strings;

import java.lang.annotation.*;

/**
 * 消息队列监听注解
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Component
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MessageQueueListener {

	/**
	 * 消息队列类型
	 *
	 * @return 消息队列类型
	 */
	String type() default Strings.EMPTY;

	/**
	 * 设置消费者组
	 *
	 * @return 消费者组名
	 */
	String group() default Strings.EMPTY;

	/**
	 * 设置消息主题
	 *
	 * @return 消息主题
	 */
	String topic() default Strings.EMPTY;

	/**
	 * 从 Broker 端批量拉取消息大小
	 *
	 * @return 默认拉取 32 条消息
	 */
	int pullBatchSize() default 0;

	/**
	 * 上报 Broker 端的最大消费消息数，当拉取消息的大小大于消费的大小时，拆成多个线程并发处理
	 *
	 * @return 默认消费 1 条消息
	 */
	int consumeMessageBatchMaxSize() default 0;

	/**
	 * 最小消费线程
	 *
	 * @return 默认 8 个线程
	 */
	int consumeThreadMin() default 0;

	/**
	 * 最大消费线程
	 *
	 * @return 默认 64 个线程
	 */
	int consumeThreadMax() default 0;

	/**
	 * 消费超时
	 *
	 * @return 默认 15 分钟
	 */
	long consumeTimeout() default 0;

	/**
	 * 消费模式
	 *
	 * @return 默认并发消费，不保证顺序
	 */
	ConsumeMode consumeMode() default ConsumeMode.UNSET;

	/**
	 * 消息模式
	 *
	 * @return 默认集群模式
	 */
	MessageModel messageModel() default MessageModel.UNSET;

	/**
	 * 消息过滤类型
	 *
	 * @return 默认按 Tag 过滤
	 */
	MessageSelectorType selectorType() default MessageSelectorType.UNSET;

	/**
	 * 消息过滤规则
	 *
	 * @return 默认全模糊匹配
	 */
	String selectorExpression() default "*";

	/**
	 * 是否开启消息轨迹追踪
	 *
	 * @return 默认开启
	 */
	boolean enableMsgTrace() default true;
}
