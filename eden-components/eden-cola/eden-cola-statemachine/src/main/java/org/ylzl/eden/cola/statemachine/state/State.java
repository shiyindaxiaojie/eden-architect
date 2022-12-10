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

import org.ylzl.eden.cola.statemachine.transition.Transition;
import org.ylzl.eden.cola.statemachine.transition.TransitionType;
import org.ylzl.eden.cola.statemachine.visitor.Visitable;

import java.util.Collection;
import java.util.List;

/**
 * 状态
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public interface State<S, E, C> extends Visitable {

	S getId();

	Transition<S, E, C> addTransition(E event, State<S, E, C> target, TransitionType transitionType);

	List<Transition<S, E, C>> getEventTransitions(E event);

	Collection<Transition<S, E, C>> getAllTransitions();
}
