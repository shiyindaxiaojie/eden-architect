package org.ylzl.eden.commons.lang.format;

/**
 * 格式处理封装
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class FormattingTuple {

	static public FormattingTuple NULL = new FormattingTuple(null);

	private final String message;
	private final Throwable throwable;
	private final Object[] argArray;

	public FormattingTuple(String message) {
		this(message, null, null);
	}

	public FormattingTuple(String message, Object[] argArray, Throwable throwable) {
		this.message = message;
		this.throwable = throwable;
		this.argArray = argArray;
	}

	public String getMessage() {
		return message;
	}

	public Object[] getArgArray() {
		return argArray;
	}

	public Throwable getThrowable() {
		return throwable;
	}
}
