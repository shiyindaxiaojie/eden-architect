package org.ylzl.eden.extension.util;

/**
 * Volatile 容器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class Holder<T> {

	private volatile T value;

	public void set(T value) {
		this.value = value;
	}

	public T get() {
		return value;
	}
}
