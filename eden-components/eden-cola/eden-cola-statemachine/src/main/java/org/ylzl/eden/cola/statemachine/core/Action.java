package org.ylzl.eden.cola.statemachine.core;

/**
 * 到达某个状态之后执行的动作
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public interface Action<S, E, C> {

	void execute(S from, S to, E event, C context);
}
