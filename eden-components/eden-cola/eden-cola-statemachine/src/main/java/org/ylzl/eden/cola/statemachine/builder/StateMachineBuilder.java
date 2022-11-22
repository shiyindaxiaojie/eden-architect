package org.ylzl.eden.cola.statemachine.builder;

import org.ylzl.eden.cola.statemachine.transition.ExternalTransition;
import org.ylzl.eden.cola.statemachine.transition.ExternalTransitions;
import org.ylzl.eden.cola.statemachine.transition.InternalTransition;

/**
 * TODO
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public interface StateMachineBuilder<S, E, C> {

	ExternalTransition<S, E, C> externalTransition();

	ExternalTransitions<S, E, C> externalTransitions();

	InternalTransition<S, E, C> internalTransition();


}
