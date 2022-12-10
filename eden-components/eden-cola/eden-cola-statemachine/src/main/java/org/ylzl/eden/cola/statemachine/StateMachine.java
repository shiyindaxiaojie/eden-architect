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

package org.ylzl.eden.cola.statemachine;

import org.ylzl.eden.cola.statemachine.callback.Callback;
import org.ylzl.eden.cola.statemachine.transition.ExternalTransition;
import org.ylzl.eden.cola.statemachine.transition.ExternalTransitions;
import org.ylzl.eden.cola.statemachine.transition.InternalTransition;
import org.ylzl.eden.cola.statemachine.visitor.Visitable;

/**
 * 状态机
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public interface StateMachine<S, E, C> extends Visitable {

	String getMachineId();

	void setMachineId(String machineId);

	boolean verify(S sourceState, E event);

	S fireEvent(S sourceState, E event, C ctx);

	Callback<S, E, C> getCallback();

	void setCallback(Callback<S, E, C> callback);

	ExternalTransition<S, E, C> externalTransition();

	ExternalTransitions<S, E, C> externalTransitions();

	InternalTransition<S, E, C> internalTransition();
}
