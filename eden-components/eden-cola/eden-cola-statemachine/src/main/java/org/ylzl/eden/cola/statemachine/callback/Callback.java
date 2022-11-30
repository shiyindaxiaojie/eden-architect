package org.ylzl.eden.cola.statemachine.callback;

/**
 * 事件回调
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public interface Callback<S, E, C> {

	void onSuccess(S sourceState, E event, C context);

	void onFail(S sourceState, E event, C context);
}
