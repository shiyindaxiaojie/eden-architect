package org.ylzl.eden.cola.statemachine.transition;

import org.ylzl.eden.cola.statemachine.dsl.To;

/**
 * 内部流转
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public interface InternalTransition<S, E, C> {

	To<S, E, C> within(S stateId);
}
