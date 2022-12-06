package org.ylzl.eden.cola.statemachine.dsl;

/**
 * 支持多个状态 {@code State} 转移到指定的一个状态 {@code State}
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public interface FromAmong<S, E, C> {

	To<S, E, C> to(S stateId);
}
