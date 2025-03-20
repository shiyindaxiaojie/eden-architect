///*
// * Copyright 2012-2019 the original author or authors.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      https://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package org.ylzl.eden.dynamic.tp.spring.boot.adapter;
//
//import com.dtp.adapter.rocketmq.RocketMqDtpAdapter;
//import com.dtp.common.ApplicationContextHolder;
//import com.dtp.common.dto.ExecutorWrapper;
//import com.dtp.common.util.ReflectionUtil;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
//import org.apache.rocketmq.client.impl.consumer.ConsumeMessageConcurrentlyService;
//import org.apache.rocketmq.client.impl.consumer.ConsumeMessageOrderlyService;
//import org.apache.rocketmq.client.impl.consumer.ConsumeMessageService;
//import org.apache.rocketmq.client.impl.consumer.DefaultMQPushConsumerImpl;
//import org.apache.rocketmq.spring.support.DefaultRocketMQListenerContainer;
//import org.ylzl.eden.common.mq.integration.rocketmq.RocketMQConsumer;
//import org.ylzl.eden.commons.collections.CollectionUtils;
//
//import java.util.Map;
//import java.util.Objects;
//import java.util.concurrent.ThreadPoolExecutor;
//
///**
// * 自定义 RocketMqDtpAdapter
// *
// * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
// * @since 2.4.x
// */
//@Slf4j
//public class CustomRocketMqDtpAdapter extends RocketMqDtpAdapter {
//
//	@Override
//	protected void initialize() {
//		Map<String, DefaultRocketMQListenerContainer> beans = ApplicationContextHolder.getBeansOfType(DefaultRocketMQListenerContainer.class);
//		if (CollectionUtils.isNotEmpty(beans)) {
//			beans.forEach((k, v) -> {
//				DefaultMQPushConsumer consumer = v.getConsumer();
//				String group = v.getConsumerGroup();
//				String topic = v.getTopic();
//				this.setThreadPoolExecutor(consumer, group, topic);
//			});
//		}
//
//		// support dynamic-mq
//		RocketMQConsumer rocketMQConsumer = ApplicationContextHolder.getBean(RocketMQConsumer.class);
//		if (rocketMQConsumer != null && !rocketMQConsumer.getConsumers().isEmpty()) {
//			rocketMQConsumer.getConsumers().forEach((k, v) -> {
//				this.setThreadPoolExecutor(v, v.getConsumerGroup(), k);
//			});
//		}
//
//		log.info("DynamicTp adapter, rocketMq consumer executors init end, executors: {}", this.executors);
//	}
//
//	private void setThreadPoolExecutor(DefaultMQPushConsumer consumer, String group, String topic) {
//		DefaultMQPushConsumerImpl pushConsumer = consumer.getDefaultMQPushConsumerImpl();
////		DefaultMQPushConsumerImpl pushConsumer = (DefaultMQPushConsumerImpl) ReflectionUtil.getFieldValue(DefaultMQPushConsumer.class, "defaultMQPushConsumerImpl", consumer);
//		if (!Objects.isNull(pushConsumer)) {
//			String key = group + "#" + topic;
//			ThreadPoolExecutor executor = null;
//			ConsumeMessageService consumeMessageService = pushConsumer.getConsumeMessageService();
//			if (consumeMessageService instanceof ConsumeMessageConcurrentlyService) {
//				executor = (ThreadPoolExecutor) ReflectionUtil.getFieldValue(ConsumeMessageConcurrentlyService.class, "consumeExecutor", consumeMessageService);
//			} else if (consumeMessageService instanceof ConsumeMessageOrderlyService) {
//				executor = (ThreadPoolExecutor) ReflectionUtil.getFieldValue(ConsumeMessageOrderlyService.class, "consumeExecutor", consumeMessageService);
//			}
//
//			if (Objects.nonNull(executor)) {
//				ExecutorWrapper executorWrapper = new ExecutorWrapper(key, executor);
//				this.initNotifyItems(key, executorWrapper);
//				this.executors.put(key, executorWrapper);
//			}
//		}
//	}
//}
