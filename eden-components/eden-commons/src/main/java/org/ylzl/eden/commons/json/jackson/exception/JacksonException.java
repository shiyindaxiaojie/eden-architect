package org.ylzl.eden.commons.json.jackson.exception;

/**
 * Jackson 处理运行时异常
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class JacksonException extends RuntimeException {

	public JacksonException(Throwable cause) {
		super(cause);
	}
}
