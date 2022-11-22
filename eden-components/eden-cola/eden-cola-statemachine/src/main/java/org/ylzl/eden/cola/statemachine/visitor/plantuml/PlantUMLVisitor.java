package org.ylzl.eden.cola.statemachine.visitor.plantuml;

import org.ylzl.eden.cola.statemachine.State;
import org.ylzl.eden.cola.statemachine.StateMachine;
import org.ylzl.eden.cola.statemachine.Transition;
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
		for(Transition<S, E, C> transition: state.getAllTransitions()){
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
