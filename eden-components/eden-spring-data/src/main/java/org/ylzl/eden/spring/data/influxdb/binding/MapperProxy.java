package org.ylzl.eden.spring.data.influxdb.binding;

import org.ylzl.eden.spring.data.influxdb.executor.parameter.ParameterHandler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Mapper 代理
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class MapperProxy<T> implements InvocationHandler {

	private Executor executor;

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		return null;
	}
}
