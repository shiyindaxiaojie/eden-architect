package org.ylzl.eden.cola.statemachine.dsl;

/**
 * TODO
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public interface From<S, E, C> {

	To<S, E, C> to(S stateId);
}
