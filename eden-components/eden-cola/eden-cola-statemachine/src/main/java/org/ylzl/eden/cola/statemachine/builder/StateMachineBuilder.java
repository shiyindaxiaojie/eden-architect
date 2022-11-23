package org.ylzl.eden.cola.statemachine.builder;

import org.ylzl.eden.cola.statemachine.State;
import org.ylzl.eden.cola.statemachine.StateMachine;
import org.ylzl.eden.cola.statemachine.StateMachineRegistry;
import org.ylzl.eden.cola.statemachine.callback.Callback;
import org.ylzl.eden.cola.statemachine.callback.RuntimeCallback;
import org.ylzl.eden.cola.statemachine.state.StateMachineImpl;
import org.ylzl.eden.cola.statemachine.transition.ExternalTransition;
import org.ylzl.eden.cola.statemachine.transition.ExternalTransitions;
import org.ylzl.eden.cola.statemachine.transition.InternalTransition;
import org.ylzl.eden.cola.statemachine.transition.TransitionType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 状态机构建器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class StateMachineBuilder<S, E, C> {

	private final Map<S, State<S, E, C>> stateMap = new ConcurrentHashMap<>();

	public ExternalTransition<S, E, C> externalTransition() {
		return new TransitionBuilder<>(stateMap, TransitionType.EXTERNAL);
	}

	public ExternalTransitions<S, E, C> externalTransitions() {
		return new TransitionsBuilder<>(stateMap, TransitionType.EXTERNAL);
	}

	public InternalTransition<S, E, C> internalTransition() {
		return new TransitionBuilder<>(stateMap, TransitionType.INTERNAL);
	}

	public StateMachine<S, E, C> build(String machineId) {
		return build(machineId, new RuntimeCallback<>());
	}

	public StateMachine<S, E, C> build(String machineId, Callback<S, E, C> callback) {
		StateMachine<S, E, C> stateMachine = new StateMachineImpl<>(stateMap, callback);
		stateMachine.setMachineId(machineId);
		StateMachineRegistry.register(stateMachine);
		return stateMachine;
	}
}
