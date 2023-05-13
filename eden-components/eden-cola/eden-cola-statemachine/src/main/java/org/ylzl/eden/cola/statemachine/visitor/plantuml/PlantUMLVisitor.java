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

package org.ylzl.eden.cola.statemachine.visitor.plantuml;

import org.ylzl.eden.cola.statemachine.StateMachine;
import org.ylzl.eden.cola.statemachine.state.State;
import org.ylzl.eden.cola.statemachine.transition.Transition;
import org.ylzl.eden.cola.statemachine.visitor.Visitor;

/**
 * PlantUML 访问器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class PlantUMLVisitor implements Visitor {

	@Override
	public <S, E, C> String visitOnEntry(StateMachine<S, E, C> stateMachine) {
		return "@startuml" + LF;
	}

	@Override
	public <S, E, C> String visitOnExit(StateMachine<S, E, C> stateMachine) {
		return "@enduml";
	}

	@Override
	public <S, E, C> String visitOnEntry(State<S, E, C> state) {
		StringBuilder sb = new StringBuilder();
		for (Transition<S, E, C> transition : state.getAllTransitions()) {
			sb.append(transition.getSource().getId())
				.append(" --> ")
				.append(transition.getTarget().getId())
				.append(" : ")
				.append(transition.getEvent())
				.append(LF);
		}
		return sb.toString();
	}

	@Override
	public <S, E, C> String visitOnExit(State<S, E, C> state) {
		return "";
	}
}
