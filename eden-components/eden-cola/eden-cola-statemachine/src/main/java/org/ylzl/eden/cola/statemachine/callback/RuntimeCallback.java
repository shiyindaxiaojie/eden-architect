package org.ylzl.eden.cola.statemachine.callback;

import org.ylzl.eden.cola.statemachine.exception.TransitionException;
import org.ylzl.eden.commons.lang.MessageFormatUtils;

/**
 * 默认事件回调处理
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class RuntimeCallback<S, E, C> implements Callback<S, E, C> {

	private static final String FIRE_EVENT_FAILED = "Cannot fire event '{}' on current state '{}' with context '{}'";

	@Override
	public void onSuccess(S sourceState, E event, C context) {}

	@Override
	public void onFail(S sourceState, E event, C context) {
		throw new TransitionException(MessageFormatUtils.format(FIRE_EVENT_FAILED, event, sourceState, context));
	}
}
