package org.ylzl.eden.cola.statemachine;

import org.ylzl.eden.cola.statemachine.visitor.Visitable;

/**
 * 状态机
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public interface StateMachine<S, E, C> extends Visitable {

	String getMachineId();

	void setMachineId(String machineId);

	boolean verify(S sourceState, E event);

	S fireEvent(S sourceState, E event, C ctx);
}
