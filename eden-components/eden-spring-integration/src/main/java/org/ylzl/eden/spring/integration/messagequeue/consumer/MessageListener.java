package org.ylzl.eden.spring.integration.messagequeue.consumer;

/**
 * 消息监听器接口
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public interface MessageListener {

	/**
	 * 消费消息
	 *
	 * @param message
	 */
	default void consume(String message){
		consume(message, () -> {});
	}

	/**
	 * 消费消息
	 *
	 * @param message
	 * @param acknowledgement
	 */
	void consume(String message, Acknowledgement acknowledgement);
}
