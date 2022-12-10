/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ylzl.eden.spring.data.mybatis.util;

import lombok.experimental.UtilityClass;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.ylzl.eden.spring.data.mybatis.spi.DataSourceUrlResolverLoader;

import javax.sql.DataSource;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Mybatis 工具集
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@UtilityClass
public class MybatisUtils {

	private static final Pattern PARAMETER_PATTERN = Pattern.compile("\\?");

	public String getDatabaseUrl(MappedStatement mappedStatement) throws NoSuchFieldException, IllegalAccessException {
		Configuration configuration = mappedStatement.getConfiguration();
		Environment environment = configuration.getEnvironment();
		DataSource dataSource = environment.getDataSource();
		return DataSourceUrlResolverLoader.parse(dataSource);
	}

	public String getMethodName(MappedStatement mappedStatement) {
		String[] strArr = mappedStatement.getId().split("\\.");
		return strArr[strArr.length - 2] + "." + strArr[strArr.length - 1];
	}

	public String getSql(MappedStatement mappedStatement, Invocation invocation) {
		Object parameter = null;
		if (invocation.getArgs().length > 1) {
			parameter = invocation.getArgs()[1];
		}
		BoundSql boundSql = mappedStatement.getBoundSql(parameter);
		Configuration configuration = mappedStatement.getConfiguration();
		return resolveSql(configuration, boundSql);
	}

	private static String resolveSql(Configuration configuration, BoundSql boundSql) {
		Object parameterObject = boundSql.getParameterObject();
		List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
		String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
		if (!parameterMappings.isEmpty() && parameterObject != null) {
			TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
			if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
				sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(resolveParameterValue(parameterObject)));

			} else {
				MetaObject metaObject = configuration.newMetaObject(parameterObject);
				Matcher matcher = PARAMETER_PATTERN.matcher(sql);
				StringBuffer sqlBuffer = new StringBuffer();
				for (ParameterMapping parameterMapping : parameterMappings) {
					String propertyName = parameterMapping.getProperty();
					Object obj = null;
					if (metaObject.hasGetter(propertyName)) {
						obj = metaObject.getValue(propertyName);
					} else if (boundSql.hasAdditionalParameter(propertyName)) {
						obj = boundSql.getAdditionalParameter(propertyName);
					}
					if (matcher.find()) {
						matcher.appendReplacement(sqlBuffer, Matcher.quoteReplacement(resolveParameterValue(obj)));
					}
				}
				matcher.appendTail(sqlBuffer);
				sql = sqlBuffer.toString();
			}
		}
		return sql;
	}

	private static String resolveParameterValue(Object obj) {
		if (obj instanceof CharSequence) {
			return "'" + obj + "'";
		}
		if (obj instanceof Date) {
			DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
			return "'" + formatter.format(obj) + "'";
		}
		return obj == null ? "" : String.valueOf(obj);
	}
}
