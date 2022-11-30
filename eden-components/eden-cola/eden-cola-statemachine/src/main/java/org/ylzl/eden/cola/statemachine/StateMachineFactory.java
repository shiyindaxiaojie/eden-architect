package org.ylzl.eden.cola.statemachine;

import org.ylzl.eden.cola.statemachine.state.StateMachineImpl;

/**
 * TODO
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class StateMachineFactory {

	public static <S, E, C> StateMachine<S, E, C> create(String machineId) {
		StateMachine<S, E, C> stateMachine = new StateMachineImpl<>();
		stateMachine.setMachineId(machineId);
		StateMachineRegistry.register(stateMachine);
		return stateMachine;
	}
}
