package org.ylzl.eden.extension.strategy;

import org.ylzl.eden.commons.lang.ArrayUtils;

import java.util.Arrays;
import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.StreamSupport;

/**
 * LoadingStrategy 容器
 *
 * @see ServiceLoader
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class LoadingStrategyHolder {

	/**
	 * ServiceLoader 加载策略
	 */
	public static volatile LoadingStrategy[] strategies = loadLoadingStrategies();

	/**
	 * 设置 ServiceLoader 加载策略
	 *
	 * @param strategies
	 */
	public static void setLoadingStrategies(LoadingStrategy... strategies) {
		if (ArrayUtils.isNotEmpty(strategies)) {
			LoadingStrategyHolder.strategies = strategies;
		}
	}

	/**
	 * 获取 ServiceLoader 加载策略
	 *
	 * @return
	 */
	public static LoadingStrategy[] loadLoadingStrategies() {
		return StreamSupport.stream(ServiceLoader.load(LoadingStrategy.class).spliterator(), false)
			.sorted()
			.toArray(LoadingStrategy[]::new);
	}

	/**
	 * 获取 ServiceLoader 加载策略
	 *
	 * @return
	 */
	public static List<LoadingStrategy> getLoadingStrategies() {
		return Arrays.asList(strategies);
	}
}
