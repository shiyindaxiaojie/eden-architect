package org.ylzl.eden.dynamic.cache.exception;

import org.ylzl.eden.commons.lang.MessageFormatUtils;

import java.util.concurrent.Callable;

/**
 * TODO
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class ValueRetrievalException extends RuntimeException {

	public ValueRetrievalException(Object key, Callable<?> loader, Throwable ex) {
		super(MessageFormatUtils.format("Value for key '{}' could not be loaded using '{}'", key, loader), ex);
	}
}
