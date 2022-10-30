package org.ylzl.eden.spring.framework.extension.strategy;

/**
 * 内部扩展点加载策略
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class InternalLoadingStrategy implements LoadingStrategy {

	public static final String META_INF_INTERNAL = "META-INF/internal/";

	@Override
	public String directory() {
		return META_INF_INTERNAL;
	}

	@Override
	public int getPriority() {
		return MAX_PRIORITY;
	}
}
