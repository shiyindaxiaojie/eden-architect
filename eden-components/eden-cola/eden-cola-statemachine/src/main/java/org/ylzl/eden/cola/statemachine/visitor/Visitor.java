package org.ylzl.eden.cola.statemachine.visitor;

import org.ylzl.eden.cola.statemachine.state.State;
import org.ylzl.eden.cola.statemachine.core.StateMachine;

/**
 * 访问器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public interface Visitor {

	char LF = '\n';

	<S, E, C> String visitOnEntry(StateMachine<S, E, C> stateMachine);

	<S, E, C> String visitOnExit(StateMachine<S, E, C> stateMachine);

	<S, E, C> String visitOnEntry(State<S, E, C> state);

	<S, E, C> String visitOnExit(State<S, E, C> state);
}
