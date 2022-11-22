package org.ylzl.eden.cola.statemachine.dsl;

import org.ylzl.eden.cola.statemachine.Action;

/**
 * TODO
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public interface When<S, E, C> {

	void perform(Action<S, E, C> action);
}
