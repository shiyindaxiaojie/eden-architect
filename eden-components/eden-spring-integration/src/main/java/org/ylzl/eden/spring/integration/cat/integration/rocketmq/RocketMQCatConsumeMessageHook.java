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

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.hook.ConsumeMessageContext;
import org.apache.rocketmq.client.hook.ConsumeMessageHook;

/**
 * RocketMQ 消费消息切入 CAT 埋点
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Slf4j
public class RocketMQCatConsumeMessageHook implements ConsumeMessageHook {

	public static final String HOOK = "RocketMQCatConsumeMessageHook";

	@Override
	public String hookName() {
		return HOOK;
	}

	@Override
	public void consumeMessageBefore(ConsumeMessageContext context) {

	}

	@Override
	public void consumeMessageAfter(ConsumeMessageContext context) {
	}
}
