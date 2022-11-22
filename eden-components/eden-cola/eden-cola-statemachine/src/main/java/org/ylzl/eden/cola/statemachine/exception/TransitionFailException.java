package org.ylzl.eden.cola.statemachine.exception;

/**
 * 流转失败
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class TransitionFailException extends RuntimeException {

	public TransitionFailException(String message) {
		super(message);
	}
}
