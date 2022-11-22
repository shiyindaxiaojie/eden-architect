package org.ylzl.eden.cola.statemachine.dsl;

import org.ylzl.eden.cola.statemachine.Condition;

/**
 * TODO
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public interface On<S, E, C> extends When<S, E, C>{

	When<S, E, C> when(Condition<C> condition);
}
