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

import org.ylzl.eden.cola.statemachine.Action;
import org.ylzl.eden.cola.statemachine.Condition;
import org.ylzl.eden.cola.statemachine.state.State;

/**
 * 从一个状态到另一个状态的流转
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public interface Transition<S, E, C> {

	State<S, E, C> getSource();

	void setSource(State<S, E, C> state);

	State<S, E, C> getTarget();

	void setTarget(State<S, E, C> target);

	E getEvent();

	void setEvent(E event);

	void setType(TransitionType type);

	Condition<C> getCondition();

	void setCondition(Condition<C> condition);

	Action<S, E, C> getAction();

	void setAction(Action<S, E, C> action);

	State<S, E, C> transit(C ctx, boolean checkCondition);

	void verify();
}
