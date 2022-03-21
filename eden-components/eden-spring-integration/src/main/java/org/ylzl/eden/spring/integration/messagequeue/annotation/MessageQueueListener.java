package org.ylzl.eden.spring.integration.messagequeue.annotation;


import org.springframework.stereotype.Component;
import org.ylzl.eden.spring.integration.messagequeue.common.MessageQueueType;

import java.lang.annotation.*;

/**
 * 消息队列监听注解
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
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
	 * @see MessageQueueType
	 */
	String type() default MessageQueueType.ROCKETMQ;

	/**
	 * 设置消息主题
	 *
	 * @return 消息主题
	 */
	String topic();

	/**
	 * 设置消费者组
	 *
	 * @return 消费者组名称
	 */
	String consumerGroup() default "${spring.application.name:default-consumer-group}";

	/**
	 * 消费标签
	 *
	 * @return 标签名称
	 */
	String tags() default "*";

	/**
	 * 命名空间
	 *
	 * @return
	 */
	String namespace() default "";
}
