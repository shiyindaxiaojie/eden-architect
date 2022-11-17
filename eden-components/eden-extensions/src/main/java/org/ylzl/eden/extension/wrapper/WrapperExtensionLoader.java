package org.ylzl.eden.extension.wrapper;

import cn.hutool.core.collection.ConcurrentHashSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

/**
 * 基于 @Wrapper 的扩展点加载器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
@Slf4j
public class WrapperExtensionLoader {

	private Set<Class<?>> cachedWrapperClasses;

	public Set<Class<?>> getCachedWrapperClasses() {
		return cachedWrapperClasses;
	}

	public void cacheWrapperClass(Class<?> clazz) {
		if (cachedWrapperClasses == null) {
			cachedWrapperClasses = new ConcurrentHashSet<>();
		}
		cachedWrapperClasses.add(clazz);
	}
}
