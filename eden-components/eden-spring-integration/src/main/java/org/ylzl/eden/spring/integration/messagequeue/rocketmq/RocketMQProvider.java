package org.ylzl.eden.spring.integration.messagequeue.rocketmq;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.ylzl.eden.spring.integration.messagequeue.core.MessageQueueProvider;
import org.ylzl.eden.spring.integration.messagequeue.core.producer.Message;
import org.ylzl.eden.spring.integration.messagequeue.core.producer.MessageQueueProducerException;
import org.ylzl.eden.spring.integration.messagequeue.core.producer.MessageSendCallback;
import org.ylzl.eden.spring.integration.messagequeue.core.producer.MessageSendResult;

import java.nio.charset.StandardCharsets;

/**
 * RockMQ 生产者
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 1.0.0
 */
@RequiredArgsConstructor
@Slf4j
public class RocketMQProvider implements MessageQueueProvider {

	private static final String ROCKETMQ_PROVIDER_CONSUME_ERROR = "RocketMQProvider send error: {}";

	private final RocketMQTemplate rocketMQTemplate;

	private final FixedRocketMQProducerProperties fixedRocketMQProducerProperties;

	/**
	 * 同步发送消息
	 *
	 * @param message
	 * @return
	 * @throws MessageQueueProducerException
	 */
	@Override
	public MessageSendResult syncSend(Message message) throws MessageQueueProducerException {
		try {
			SendResult sendResult = rocketMQTemplate.getProducer().send(transfer(message));
			return transfer(sendResult);
		} catch (Exception e) {
			log.error(ROCKETMQ_PROVIDER_CONSUME_ERROR, e.getMessage(), e);
			throw new MessageQueueProducerException(e.getMessage());
		}
	}

	/**
	 * 异步发送消息
	 *
	 * @param message
	 * @param messageCallback
	 * @throws MessageQueueProducerException
	 */
	@Override
	public void asyncSend(Message message, MessageSendCallback messageCallback) throws MessageQueueProducerException {
		DefaultMQProducer producer = rocketMQTemplate.getProducer();
		if (StringUtils.isNotBlank(message.getNamespace())) {
			producer.setNamespace(message.getNamespace());
		} else if (StringUtils.isNotBlank(fixedRocketMQProducerProperties.getNamespace())) {
			producer.setNamespace(fixedRocketMQProducerProperties.getNamespace());
		}
		try {
			producer.send(transfer(message), new SendCallback() {

				@Override
				public void onSuccess(SendResult sendResult) {
					messageCallback.onSuccess(transfer(sendResult));
				}

				@Override
				public void onException(Throwable e) {
					messageCallback.onFailed(e);
				}
			});
		} catch (Exception e) {
			log.error(ROCKETMQ_PROVIDER_CONSUME_ERROR, e.getMessage(), e);
			throw new MessageQueueProducerException(e.getMessage());
		}
	}

	/**
	 * 转换为 RocketMQ 消息
	 *
	 * @param message
	 * @return
	 */
	private org.apache.rocketmq.common.message.Message transfer(Message message) {
		org.apache.rocketmq.common.message.Message rocketMsg =
			new org.apache.rocketmq.common.message.Message(message.getTopic(), message.getTags(),
				message.getKey(), message.getBody().getBytes(StandardCharsets.UTF_8));
		if (message.getDelayTimeLevel() > 0) {
			rocketMsg.setDelayTimeLevel(message.getDelayTimeLevel());
		}
		return rocketMsg;
	}

	/**
	 * 转化为自定义的 MessageSendResult
	 *
	 * @param sendResult
	 * @return
	 */
	private MessageSendResult transfer(SendResult sendResult) {
		return MessageSendResult.builder()
			.topic(sendResult.getMessageQueue().getTopic())
			.partition(sendResult.getMessageQueue().getQueueId())
			.offset(sendResult.getQueueOffset())
			.transactionId(sendResult.getTransactionId())
			.build();
	}
}
