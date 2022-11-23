package org.ylzl.eden.cola.statemachine.exception;

/**
 * 流转异常
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class TransitionException extends RuntimeException {

	public TransitionException(String message) {
		super(message);
	}
}
