package org.ylzl.eden.cola.statemachine.transition.builder;

import lombok.RequiredArgsConstructor;
import org.ylzl.eden.cola.statemachine.Action;
import org.ylzl.eden.cola.statemachine.Condition;
import org.ylzl.eden.cola.statemachine.state.State;
import org.ylzl.eden.cola.statemachine.transition.Transition;
import org.ylzl.eden.cola.statemachine.dsl.FromAmong;
import org.ylzl.eden.cola.statemachine.dsl.On;
import org.ylzl.eden.cola.statemachine.dsl.To;
import org.ylzl.eden.cola.statemachine.dsl.When;
import org.ylzl.eden.cola.statemachine.state.StateStore;
import org.ylzl.eden.cola.statemachine.transition.ExternalTransitions;
import org.ylzl.eden.cola.statemachine.transition.TransitionType;

import java.util.ArrayList;
import java.util.List;

/**
 * {@code Transitions} 构建器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
public class TransitionsBuilder<S, E, C> implements ExternalTransitions<S, E, C>, FromAmong<S, E, C>, On<S, E, C>, To<S, E, C> {

	private final List<State<S, E, C>> sources = new ArrayList<>();

	private State<S, E, C> target;

	private final List<Transition<S, E, C>> transitions = new ArrayList<>();

	private final StateStore<S, E, C> stateStore;

	private final TransitionType transitionType;

	@SafeVarargs
	@Override
	public final FromAmong<S, E, C> fromAmong(S... stateIds) {
		for (S stateId : stateIds) {
			sources.add(stateStore.getState(stateId));
		}
		return this;
	}

	@Override
	public To<S, E, C> to(S stateId) {
		this.target = stateStore.getState(stateId);
		return this;
	}

	@Override
	public On<S, E, C> on(E event) {
		for (State<S, E, C> source : sources) {
			Transition<S, E, C> transition = source.addTransition(event, target, transitionType);
			transitions.add(transition);
		}
		return this;
	}

	@Override
	public When<S, E, C> when(Condition<C> condition) {
		for (Transition<S, E, C> transition : transitions) {
			transition.setCondition(condition);
		}
		return this;
	}

	@Override
	public void perform(Action<S, E, C> action) {
		for (Transition<S, E, C> transition : transitions) {
			transition.setAction(action);
		}
	}
}
