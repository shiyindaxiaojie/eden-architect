package org.ylzl.eden.spring.data.influxdb.executor;

import liquibase.pro.packaged.T;

import java.util.List;

/**
 * 执行器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public interface Executor {

	List<T> query(Class<T> clazz);

	void save(final T model);

	void save(final List<T> modelList);

	void delete(final T modelList);

	List<T> query(final String sql, Class<T> clazz);

	void delete(final String sql, Class<T> clazz);
}
