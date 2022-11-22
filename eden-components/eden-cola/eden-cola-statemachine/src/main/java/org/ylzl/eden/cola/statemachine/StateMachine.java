package org.ylzl.eden.cola.statemachine;

/**
 * 状态机
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public interface StateMachine<S, E, C> extends Visitable {

	boolean verify(S sourceStateId,E event);
}
