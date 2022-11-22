package org.ylzl.eden.cola.statemachine.transition;

import org.ylzl.eden.cola.statemachine.dsl.From;

/**
 * 外部流转
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public interface ExternalTransition<S, E, C> {

	From<S, E, C> from(S stateId);
}
