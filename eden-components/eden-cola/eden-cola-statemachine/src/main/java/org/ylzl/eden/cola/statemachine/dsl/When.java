package org.ylzl.eden.cola.statemachine.dsl;

import org.ylzl.eden.cola.statemachine.Action;

/**
 * 当 {@code Condition} 成立时执行某个动作 {@code Action}
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public interface When<S, E, C> {

	void perform(Action<S, E, C> action);
}
