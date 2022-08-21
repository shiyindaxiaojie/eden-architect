package org.ylzl.eden.spring.cloud.cat.integration.mybatis.interceptor;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Message;
import com.dianping.cat.message.Transaction;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.ylzl.eden.spring.cloud.cat.integration.mybatis.spi.DataSourceUrlResolverSupport;

import javax.sql.DataSource;
import java.sql.Statement;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Mybatis 集成 CAT 插件
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Intercepts({
	@Signature(type = StatementHandler.class, method = "select", args = {Statement.class, ResultHandler.class}),
	@Signature(type = StatementHandler.class, method = "insert", args = Statement.class),
	@Signature(type = StatementHandler.class, method = "update", args = Statement.class),
	@Signature(type = StatementHandler.class, method = "delete", args = Statement.class),
	@Signature(type = StatementHandler.class, method = "find", args = {Statement.class, ResultHandler.class}),
	@Signature(type = StatementHandler.class, method = "save", args = Statement.class),
	@Signature(type = StatementHandler.class, method = "batch", args = Statement.class)
})
public class CatMybatisInterceptor implements Interceptor {

	public static final String TYPE_SQL = "SQL";

	public static final String TYPE_SQL_DATABASE = "SQL.database";

	public static final String TYPE_SQL_METHOD = "SQL.method";

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
		String methodName = getMethodName(mappedStatement);
		Transaction transaction = Cat.newTransaction(TYPE_SQL, methodName);

		String dataSourceUrl = getSQLDatabaseUrl(mappedStatement);
		Cat.logEvent(TYPE_SQL_DATABASE, dataSourceUrl);
		Cat.logEvent(TYPE_SQL_METHOD, mappedStatement.getSqlCommandType().name().toLowerCase(),
			Message.SUCCESS, getSql(invocation, mappedStatement));
		try {
			Object returnValue = invocation.proceed();
			transaction.setStatus(Transaction.SUCCESS);
			return returnValue;
		} catch (Throwable e) {
			Cat.logError(e);
			transaction.setStatus(e);
			throw e;
		} finally {
			transaction.complete();
		}
	}

	private String getSQLDatabaseUrl(MappedStatement mappedStatement) {
		Configuration configuration = mappedStatement.getConfiguration();
		Environment environment = configuration.getEnvironment();
		DataSource dataSource = environment.getDataSource();
		return DataSourceUrlResolverSupport.resolve(dataSource);
	}

	private String getMethodName(MappedStatement mappedStatement) {
		String[] strArr = mappedStatement.getId().split("\\.");
		return strArr[strArr.length - 2] + "." + strArr[strArr.length - 1];
	}

	private String getSql(Invocation invocation, MappedStatement mappedStatement) {
		Object parameter = null;
		if (invocation.getArgs().length > 1) {
			parameter = invocation.getArgs()[1];
		}
		BoundSql boundSql = mappedStatement.getBoundSql(parameter);
		Configuration configuration = mappedStatement.getConfiguration();
		return showSql(configuration, boundSql);
	}

	private static String showSql(Configuration configuration, BoundSql boundSql) {
		Object parameterObject = boundSql.getParameterObject();
		List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
		String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
		StringBuilder sqlBuilder = new StringBuilder(sql);
		if (parameterMappings.size() > 0 && parameterObject != null) {
			int start = sqlBuilder.indexOf("?");
			int end = start + 1;

			TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
			if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
				sqlBuilder.replace(start, end, getParameterValue(parameterObject));
			} else {
				MetaObject metaObject = configuration.newMetaObject(parameterObject);
				for (ParameterMapping parameterMapping : parameterMappings) {
					String propertyName = parameterMapping.getProperty();
					if (metaObject.hasGetter(propertyName)) {
						Object obj = metaObject.getValue(propertyName);
						sqlBuilder.replace(start, end, getParameterValue(obj));
					} else if (boundSql.hasAdditionalParameter(propertyName)) {
						Object obj = boundSql.getAdditionalParameter(propertyName);
						sqlBuilder.replace(start, end, getParameterValue(obj));
					}

					start = sqlBuilder.indexOf("?");
					end = start + 1;
				}
			}
		}
		return sqlBuilder.toString();
	}

	private static String getParameterValue(Object obj) {
		StringBuilder retStringBuilder = new StringBuilder();
		if (obj instanceof String) {
			retStringBuilder.append("'").append(obj).append("'");
		} else if (obj instanceof Date) {
			DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
			retStringBuilder.append("'").append(formatter.format(new Date())).append("'");
		} else {
			retStringBuilder.append("'").append(obj == null ? "" : obj).append("'");
		}
		return retStringBuilder.toString();
	}
}
