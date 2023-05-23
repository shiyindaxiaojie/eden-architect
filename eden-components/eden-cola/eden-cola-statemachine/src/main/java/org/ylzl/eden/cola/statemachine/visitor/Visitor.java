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

package org.ylzl.eden.cola.statemachine.visitor;

import org.ylzl.eden.cola.statemachine.StateMachine;
import org.ylzl.eden.cola.statemachine.state.State;

/**
 * 访问器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public interface Visitor {

	char LF = '\n';

	<S, E, C> String visitOnEntry(StateMachine<S, E, C> stateMachine);

	<S, E, C> String visitOnExit(StateMachine<S, E, C> stateMachine);

	<S, E, C> String visitOnEntry(State<S, E, C> state);

	<S, E, C> String visitOnExit(State<S, E, C> state);
}
