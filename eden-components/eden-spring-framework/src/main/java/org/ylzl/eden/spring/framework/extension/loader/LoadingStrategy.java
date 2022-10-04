package org.ylzl.eden.spring.framework.extension.loader;

/**
 * 扩展点加载策略接口
 *
 * @author <a href="mailto:guoyuanlu@puyiwm.com">gyl</a>
 * @since 1.0.0
 */
public interface LoadingStrategy extends Prioritized {

	String directory();

	default String getName() {
		return this.getClass().getSimpleName();
	}

	String ALL = "ALL";
}
