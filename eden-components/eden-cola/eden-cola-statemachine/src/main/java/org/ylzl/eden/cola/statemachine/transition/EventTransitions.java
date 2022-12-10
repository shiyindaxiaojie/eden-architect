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

package org.ylzl.eden.cola.statemachine.transition;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.ylzl.eden.cola.statemachine.exception.StateMachineException;

import java.util.List;
import java.util.Map;

/**
 * 同一个 Event 可以触发多个 Transitions
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class EventTransitions<S, E, C> {

	private final Map<E, List<Transition<S, E, C>>> eventTransitions = Maps.newHashMap();

	public List<Transition<S, E, C>> get(E event) {
		return eventTransitions.get(event);
	}

	public void put(E event, Transition<S, E, C> transition) {
		if (eventTransitions.get(event) == null) {
			List<Transition<S, E, C>> transitions = Lists.newArrayList();
			transitions.add(transition);
			eventTransitions.put(event, transitions);
		} else {
			List<Transition<S, E, C>> existingTransitions = eventTransitions.get(event);
			verify(existingTransitions, transition);
			existingTransitions.add(transition);
		}
	}

	public List<Transition<S, E, C>> allTransitions() {
		List<Transition<S, E, C>> allTransitions = Lists.newArrayList();
		for (List<Transition<S, E, C>> transitions : eventTransitions.values()) {
			allTransitions.addAll(transitions);
		}
		return allTransitions;
	}

	private void verify(List<Transition<S, E, C>> existingTransitions, Transition<S, E, C> newTransition) {
		for (Transition<S, E, C> transition : existingTransitions) {
			if (transition.equals(newTransition)) {
				throw new StateMachineException(transition + " already exist, you can not add another one");
			}
		}
	}
}
