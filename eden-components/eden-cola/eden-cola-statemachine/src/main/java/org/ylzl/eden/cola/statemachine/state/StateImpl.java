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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ylzl.eden.cola.statemachine.transition.EventTransitions;
import org.ylzl.eden.cola.statemachine.transition.Transition;
import org.ylzl.eden.cola.statemachine.transition.TransitionImpl;
import org.ylzl.eden.cola.statemachine.transition.TransitionType;
import org.ylzl.eden.cola.statemachine.visitor.Visitor;

import java.util.Collection;
import java.util.List;

/**
 * 状态实现
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
@Slf4j
public class StateImpl<S, E, C> implements State<S, E, C> {

	private static final String ADD_NEW_TRANSITION = "Add new transition '{}'";

	private final EventTransitions<S, E, C> eventTransitions = new EventTransitions<>();

	private final S stateId;

	@Override
	public S getId() {
		return stateId;
	}

	@Override
	public String accept(Visitor visitor) {
		String entry = visitor.visitOnEntry(this);
		String exit = visitor.visitOnExit(this);
		return entry + exit;
	}

	@Override
	public Transition<S, E, C> addTransition(E event, State<S, E, C> target, TransitionType transitionType) {
		Transition<S, E, C> newTransition = new TransitionImpl<>();
		newTransition.setSource(this);
		newTransition.setTarget(target);
		newTransition.setEvent(event);
		newTransition.setType(transitionType);

		log.debug(ADD_NEW_TRANSITION, newTransition);
		eventTransitions.put(event, newTransition);
		return newTransition;
	}

	@Override
	public List<Transition<S, E, C>> getEventTransitions(E event) {
		return eventTransitions.get(event);
	}

	@Override
	public Collection<Transition<S, E, C>> getAllTransitions() {
		return eventTransitions.allTransitions();
	}
}
