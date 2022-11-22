package org.ylzl.eden.cola.statemachine.dsl;

/**
 * TODO
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public interface To<S, E, C> {

	On<S, E, C> on(E event);
}
