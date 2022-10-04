package org.ylzl.eden.spring.framework.extension.loader;

/**
 * 内部扩展点加载策略
 *
 * @author <a href="mailto:guoyuanlu@puyiwm.com">gyl</a>
 * @since 1.0.0
 */
public class InternalLoadingStrategy implements LoadingStrategy {

	public static final String META_INF_INTERNAL = "META-INF/internal/";

	public static final String INTERNAL = "INTERNAL";

	@Override
	public String directory() {
		return META_INF_INTERNAL;
	}

	@Override
	public String getName() {
		return INTERNAL;
	}

	@Override
	public int getPriority() {
		return NORMAL_PRIORITY;
	}
}
