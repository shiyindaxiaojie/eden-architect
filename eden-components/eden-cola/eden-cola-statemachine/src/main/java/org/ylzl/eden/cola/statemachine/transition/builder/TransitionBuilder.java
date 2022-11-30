package org.ylzl.eden.cola.statemachine.transition.builder;

import lombok.RequiredArgsConstructor;
import org.ylzl.eden.cola.statemachine.core.Action;
import org.ylzl.eden.cola.statemachine.core.Condition;
import org.ylzl.eden.cola.statemachine.state.State;
import org.ylzl.eden.cola.statemachine.transition.Transition;
import org.ylzl.eden.cola.statemachine.dsl.From;
import org.ylzl.eden.cola.statemachine.dsl.On;
import org.ylzl.eden.cola.statemachine.dsl.To;
import org.ylzl.eden.cola.statemachine.dsl.When;
import org.ylzl.eden.cola.statemachine.state.StateStore;
import org.ylzl.eden.cola.statemachine.transition.ExternalTransition;
import org.ylzl.eden.cola.statemachine.transition.InternalTransition;
import org.ylzl.eden.cola.statemachine.transition.TransitionType;

/**
 * {@code Transition} 构建器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
public class TransitionBuilder<S, E, C> implements ExternalTransition<S, E, C>, InternalTransition<S, E, C>, From<S, E, C>, On<S, E, C>, To<S, E, C> {

	private State<S, E, C> source;

	private State<S, E, C> target;

	private Transition<S, E, C> transition;

	private final StateStore<S, E, C> stateStore;

	private final TransitionType transitionType;

	@Override
	public From<S, E, C> from(S stateId) {
		this.source = stateStore.getState(stateId);
		return this;
	}

	@Override
	public To<S, E, C> to(S stateId) {
		this.target = stateStore.getState(stateId);
		return this;
	}

	@Override
	public To<S, E, C> within(S stateId) {
		this.source = this.target = stateStore.getState(stateId);
		return this;
	}

	@Override
	public On<S, E, C> on(E event) {
		this.transition = source.addTransition(event, target, transitionType);
		return this;
	}

	@Override
	public When<S, E, C> when(Condition<C> condition) {
		this.transition.setCondition(condition);
		return this;
	}

	@Override
	public void perform(Action<S, E, C> action) {
		this.transition.setAction(action);
	}
}
