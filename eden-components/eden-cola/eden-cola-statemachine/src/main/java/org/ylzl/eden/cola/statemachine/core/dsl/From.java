package org.ylzl.eden.cola.statemachine.core.dsl;

/**
 * 从当前状态 {@code State} 转移到指定的状态 {@code State}
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public interface From<S, E, C> {

	To<S, E, C> to(S stateId);
}
