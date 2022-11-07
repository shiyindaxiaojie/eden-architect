package org.ylzl.eden.common.cache.value;

import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * Null 值封装
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@EqualsAndHashCode
public final class NullValue implements Serializable {

	public static final Object INSTANCE = new NullValue();

	private NullValue() {
	}

	private Object readResolve() {
		return INSTANCE;
	}

	@Override
	public String toString() {
		return "NULL";
	}
}
