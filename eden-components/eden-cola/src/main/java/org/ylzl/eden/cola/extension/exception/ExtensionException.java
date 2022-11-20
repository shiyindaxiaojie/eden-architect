package org.ylzl.eden.cola.extension.exception;

import org.ylzl.eden.spring.framework.error.BaseException;

/**
 * 扩展点异常
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class ExtensionException extends BaseException {

	public ExtensionException(String errMessage, Object... params) {
		super("EXT-FOUND-404", errMessage, params);
	}
}
