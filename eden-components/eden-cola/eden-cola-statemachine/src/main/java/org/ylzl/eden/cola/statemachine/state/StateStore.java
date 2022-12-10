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

package org.ylzl.eden.cola.statemachine.state;

import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 状态存储
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class StateStore<S, E, C> {

	@Getter
	private final Map<S, State<S, E, C>> stateMap = new ConcurrentHashMap<>();

	public State<S, E, C> getState(S stateId) {
		State<S, E, C> state = stateMap.get(stateId);
		if (state == null) {
			state = new StateImpl<>(stateId);
			stateMap.putIfAbsent(stateId, state);
		}
		return state;
	}
}
