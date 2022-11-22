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

	boolean verify(S sourceStateId, E event);

	S fireEvent(S sourceState, E event, C ctx);

	String generateUML();
}
