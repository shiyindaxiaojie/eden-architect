package org.ylzl.eden.commons.lang.format;

import lombok.Getter;

/**
 * 格式处理封装
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Getter
public class FormattingTuple {

	public static final FormattingTuple NULL = new FormattingTuple(null);

	private final String message;

	private final Object[] argArray;

	private final Throwable throwable;

	public FormattingTuple(String message) {
		this(message, null, null);
	}

	public FormattingTuple(String message, Object[] argArray, Throwable throwable) {
		this.message = message;
		this.argArray = argArray;
		this.throwable = throwable;
	}
}
