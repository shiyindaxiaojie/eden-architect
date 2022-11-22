package org.ylzl.eden.cola.statemachine;

/**
 * 状态上下文
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public interface StateContext<S, E, C> {

	Transition<S, E, C> getTransition();

	StateMachine<S, E, C> getStateMachine();
}
