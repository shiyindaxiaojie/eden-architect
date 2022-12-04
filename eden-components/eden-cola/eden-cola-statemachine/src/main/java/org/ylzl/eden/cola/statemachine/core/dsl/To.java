package org.ylzl.eden.cola.statemachine.core.dsl;

/**
 * 当 {@code Event} 事件触发时，并且 {@code Condition} 条件成立，将到达当前指定的状态 {@code State}
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public interface To<S, E, C> {

	On<S, E, C> on(E event);
}
