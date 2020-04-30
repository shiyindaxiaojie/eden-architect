/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ylzl.eden.sample.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.ylzl.eden.sample.domain.User;
import org.ylzl.eden.spring.boot.commons.json.JacksonUtils;

import java.util.List;

/**
 * 生产者
 *
 * @author gyl
 * @since 1.0.0
 */
@Component
public class UserProducer {

	private final KafkaTemplate<String, String> kafkaTemplate;

	public UserProducer(KafkaTemplate<String, String> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	public ListenableFuture<SendResult<String, String>> send(User user) throws JsonProcessingException {
		String data = JacksonUtils.toJSONString(user);
		ListenableFuture<SendResult<String, String>> sendResult = kafkaTemplate.send("sample_users", user.getLogin(), data);
		sendResult.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {

			@Override
			public void onFailure(Throwable throwable) {

			}

			@Override
			public void onSuccess(SendResult<String, String> sendResult) {

			}
		});
		return sendResult;
	}
}
