package org.ylzl.eden.cola.statemachine;

/**
 * 到达某个状态的条件
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public interface Condition<C> {

	boolean isSatisfied(C context);
}
