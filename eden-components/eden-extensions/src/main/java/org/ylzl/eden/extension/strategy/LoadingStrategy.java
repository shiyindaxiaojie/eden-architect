package org.ylzl.eden.extension.strategy;

/**
 * 扩展点加载策略
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public interface LoadingStrategy extends Prioritized {

	String directory();

	default boolean preferExtensionClassLoader() {
		return false;
	}

	default String[] excludedPackages() {
		return null;
	}

	default boolean overridden() {
		return false;
	}
}
