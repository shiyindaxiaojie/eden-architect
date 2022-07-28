package org.ylzl.eden.spring.data.influxdb.binding;

import lombok.RequiredArgsConstructor;

import java.lang.reflect.Proxy;

/**
 * TODO
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@RequiredArgsConstructor
public class MapperProxyFactory<T> {

	private final Class<T> mapperInterface;

//	public T newInstance(Executor executor) {
//		final MapperProxy<T> mapperProxy = new MapperProxy<>(executor, mapperInterface);
//		return newInstance(mapperProxy);
//	}

	protected T newInstance(MapperProxy<T> mapperProxy) {
		return (T) Proxy.newProxyInstance(mapperInterface.getClassLoader(),
			new Class[] { mapperInterface }, mapperProxy);
	}
}
