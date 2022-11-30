package org.ylzl.eden.cola.statemachine;

import org.ylzl.eden.cola.statemachine.callback.Callback;
import org.ylzl.eden.cola.statemachine.transition.ExternalTransition;
import org.ylzl.eden.cola.statemachine.transition.ExternalTransitions;
import org.ylzl.eden.cola.statemachine.transition.InternalTransition;
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

	Callback<S, E, C> getCallback();

	void setCallback(Callback<S, E, C> callback);

	ExternalTransition<S, E, C> externalTransition();

	ExternalTransitions<S, E, C> externalTransitions();

	InternalTransition<S, E, C> internalTransition();
}
