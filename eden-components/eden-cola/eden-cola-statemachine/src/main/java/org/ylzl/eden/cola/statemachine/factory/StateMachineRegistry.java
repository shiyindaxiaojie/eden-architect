package org.ylzl.eden.cola.statemachine.factory;

import com.google.common.collect.Maps;
import org.ylzl.eden.cola.statemachine.StateMachine;
import org.ylzl.eden.cola.statemachine.exception.StateMachineException;
import org.ylzl.eden.commons.lang.MessageFormatUtils;

import java.util.Map;

/**
 * 状态机注册器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class StateMachineRegistry {

	private static final String BUILD_FAILED = "The state machine with id '{}' is already built, no need to build again";

	private static final String INSTANCE_NOT_FOUND = "here is no stateMachine instance for '{}', please build it first";

	private static final Map<String, StateMachine<?, ?, ?>> stateMachineMap = Maps.newConcurrentMap();

	public static void register(StateMachine<?, ?, ?> stateMachine) {
		String machineId = stateMachine.getMachineId();
		if (stateMachineMap.get(machineId) != null) {
			throw new StateMachineException(MessageFormatUtils.format(BUILD_FAILED, machineId));
		}
		stateMachineMap.put(stateMachine.getMachineId(), stateMachine);
	}

	public static StateMachine<?, ?, ?> get(String machineId) {
		StateMachine<?, ?, ?> stateMachine = stateMachineMap.get(machineId);
		if (stateMachine == null) {
			throw new StateMachineException(MessageFormatUtils.format(INSTANCE_NOT_FOUND, machineId));
		}
		return stateMachine;
	}
}
