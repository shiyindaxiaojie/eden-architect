package org.ylzl.eden.cache.adapter.core.value;

import java.io.Serializable;

/**
 * Null 值封装
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public final class NullValue implements Serializable {

	public static final Object INSTANCE = new NullValue();

	private NullValue() {
	}

	private Object readResolve() {
		return INSTANCE;
	}

	@Override
	public boolean equals(Object obj) {
		return (this == obj || obj == null);
	}

	@Override
	public int hashCode() {
		return NullValue.class.hashCode();
	}

	@Override
	public String toString() {
		return "NULL";
	}
}
