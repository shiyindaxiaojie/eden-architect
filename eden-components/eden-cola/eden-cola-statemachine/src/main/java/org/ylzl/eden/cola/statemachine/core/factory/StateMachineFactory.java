package org.ylzl.eden.cola.statemachine.core.factory;

import org.ylzl.eden.cola.statemachine.core.StateMachine;

/**
 * 状态机工厂
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
