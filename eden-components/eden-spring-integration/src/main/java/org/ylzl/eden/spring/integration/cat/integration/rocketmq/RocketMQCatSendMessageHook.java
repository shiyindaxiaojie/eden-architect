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

package org.ylzl.eden.spring.integration.cat.integration.rocketmq;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.hook.SendMessageContext;
import org.apache.rocketmq.client.hook.SendMessageHook;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.client.trace.AsyncTraceDispatcher;
import org.apache.rocketmq.client.trace.TraceContext;
import org.apache.rocketmq.client.trace.TraceDispatcher;
import org.ylzl.eden.spring.integration.cat.CatConstants;

/**
 * RocketMQ 发送消息切入 CAT 埋点
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@RequiredArgsConstructor
@Slf4j
public class RocketMQCatSendMessageHook implements SendMessageHook {

	private static final String HOOK = "RocketMQCatSendMessageHook";

	private final TraceDispatcher localDispatcher;

	@Override
	public String hookName() {
		return HOOK;
	}

	@Override
	public void sendMessageBefore(SendMessageContext context) {
		//if it is message trace data,then it doesn't recorded
		if (context == null || context.getMessage().getTopic().startsWith(((AsyncTraceDispatcher) localDispatcher).getTraceTopicName())) {
			return;
		}

		context.setMqTraceContext(new TraceContext()); // 默认初始化时间戳
	}

	@Override
	public void sendMessageAfter(SendMessageContext context) {
		//if it is message trace data,then it doesn't recorded
		if (context == null || context.getMessage().getTopic().startsWith(((AsyncTraceDispatcher) localDispatcher).getTraceTopicName())
			|| context.getMqTraceContext() == null) {
			return;
		}
		if (context.getSendResult() == null) {
			return;
		}
		if (context.getSendResult().getRegionId() == null || !context.getSendResult().isTraceOn()) {
			// if switch is false,skip it
			return;
		}

		TraceContext tuxeContext = (TraceContext) context.getMqTraceContext();
		long costTime = System.currentTimeMillis() - tuxeContext.getTimeStamp();

		String name = context.getNamespace() + context.getMessage().getTopic();
		Transaction transaction = Cat.newTransaction(CatConstants.TYPE_MQ_PRODUCER, name);
		transaction.addData(CatConstants.DATA_COMPONENT, CatConstants.DATA_COMPONENT_ROCKETMQ);
		transaction.setDurationInMillis(costTime);
		if (context.getSendResult().getSendStatus().equals(SendStatus.SEND_OK)) {
			transaction.setSuccessStatus();
		} else {
			transaction.setStatus(context.getSendResult().getSendStatus().name());
		}
		Cat.logEvent(CatConstants.TYPE_MQ_PRODUCER_TOPIC, context.getMq().getTopic());
		Cat.logEvent(CatConstants.TYPE_MQ_PRODUCER_ADDR, context.getBrokerAddr());
		transaction.complete();
	}
}
