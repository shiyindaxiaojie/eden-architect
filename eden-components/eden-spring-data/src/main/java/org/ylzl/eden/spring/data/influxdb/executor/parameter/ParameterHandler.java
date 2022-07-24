package org.ylzl.eden.spring.data.influxdb.executor.parameter;

import java.lang.reflect.Parameter;

/**
 * 参数处理
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class ParameterHandler {

	public String handleParameter(Parameter[] parameters, Object[] args, String sql) {
		for (int i = 0; i < parameters.length; i++) {
			Class<?> parameterType = parameters[i].getType();
			String parameterName = parameters[i].getName();

			sql = sql.replaceAll("\\#\\{" + parameterName + "\\}", parameterType == String.class?
				"'" + args[i] + "'" : args[i].toString());
			sql = sql.replaceAll("\\$\\{" + parameterName + "\\}", args[i].toString());
		}
		return sql;
	}
}
