package org.ylzl.eden.spring.framework.extension.loader;

/**
 * 扩展点加载策略接口
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public interface LoadingStrategy extends Prioritized {

	String ALL = "ALL";

	String directory();

	default String getName() {
		return this.getClass().getSimpleName();
	}

	default boolean preferExtensionClassLoader() {
		return false;
	}
}
