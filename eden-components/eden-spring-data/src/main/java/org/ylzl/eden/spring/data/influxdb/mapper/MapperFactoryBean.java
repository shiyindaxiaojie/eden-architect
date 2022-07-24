package org.ylzl.eden.spring.data.influxdb.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.Proxy;

/**
 * TODO
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@RequiredArgsConstructor
public class MapperFactoryBean<T> implements FactoryBean<T> {

	private Class<T> interfaceClass;

	@Override
	public T getObject() throws Exception {
		return Proxy.newProxyInstance(
			interfaceClass.getClassLoader(), new Class[]{ interfaceClass },
			new ProxyMapper(executor, new ParameterHandler(), new ResultSetHandler()));
	}

	@Override
	public Class<?> getObjectType() {
		return interfaceClass;
	}
}
