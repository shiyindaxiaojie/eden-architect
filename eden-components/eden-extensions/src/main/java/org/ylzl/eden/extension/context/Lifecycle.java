package org.ylzl.eden.extension.context;

/**
 * 组件生命周期
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public interface Lifecycle {

	void initialize() throws IllegalStateException;

	void start() throws IllegalStateException;

	void destroy() throws IllegalStateException;
}
