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
import org.ylzl.eden.commons.lang.Strings;
import org.ylzl.eden.common.mq.consumer.MessageModel;
import org.ylzl.eden.common.mq.consumer.MessageSelectorType;

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
	 * 批量拉取消息大小
	 *
	 * @return 拉取大小
	 */
	int pullBatchSize() default 0;

	/**
	 * 消费消息批次上限大小，当拉取消息的大小大于消费的大小时，拆成多个线程并发处理
	 */
	int consumeMessageBatchMaxSize() default 0;

	/**
	 * 消息模式
	 *
	 * @return 消息模式
	 */
	String messageModel() default MessageModel.CLUSTERING;

	/**
	 * 消息过滤类型
	 *
	 * @return 消息过滤类型
	 */
	String selectorType() default MessageSelectorType.TAG;

	/**
	 * 消息过滤规则
	 *
	 * @return 消息过滤规则
	 */
	String selectorExpression() default "*";
}
