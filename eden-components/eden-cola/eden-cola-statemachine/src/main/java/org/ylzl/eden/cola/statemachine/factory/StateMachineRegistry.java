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

package org.ylzl.eden.cola.statemachine.factory;

import com.google.common.collect.Maps;
import org.ylzl.eden.cola.statemachine.StateMachine;
import org.ylzl.eden.cola.statemachine.exception.StateMachineException;
import org.ylzl.eden.commons.lang.MessageFormatUtils;

import java.util.Map;

/**
 * 状态机注册器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class StateMachineRegistry {

	private static final String BUILD_FAILED = "The state machine with id '{}' is already built, no need to build again";

	private static final String INSTANCE_NOT_FOUND = "here is no stateMachine instance for '{}', please build it first";

	private static final Map<String, StateMachine<?, ?, ?>> stateMachineMap = Maps.newConcurrentMap();

	public static void register(StateMachine<?, ?, ?> stateMachine) {
		String machineId = stateMachine.getMachineId();
		if (stateMachineMap.get(machineId) != null) {
			throw new StateMachineException(MessageFormatUtils.format(BUILD_FAILED, machineId));
		}
		stateMachineMap.put(stateMachine.getMachineId(), stateMachine);
	}

	public static StateMachine<?, ?, ?> get(String machineId) {
		StateMachine<?, ?, ?> stateMachine = stateMachineMap.get(machineId);
		if (stateMachine == null) {
			throw new StateMachineException(MessageFormatUtils.format(INSTANCE_NOT_FOUND, machineId));
		}
		return stateMachine;
	}
}
