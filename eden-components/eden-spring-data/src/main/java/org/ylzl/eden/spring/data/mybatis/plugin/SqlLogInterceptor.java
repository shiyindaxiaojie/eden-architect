package org.ylzl.eden.spring.data.mybatis.plugin;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.SystemClock;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.ResultHandler;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * SQL 日志拦截器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Slf4j
@Intercepts({
	@Signature(type = StatementHandler.class, method = "select", args = {Statement.class, ResultHandler.class}),
	@Signature(type = StatementHandler.class, method = "insert", args = Statement.class),
	@Signature(type = StatementHandler.class, method = "update", args = Statement.class),
	@Signature(type = StatementHandler.class, method = "delete", args = Statement.class),
	@Signature(type = StatementHandler.class, method = "find", args = {Statement.class, ResultHandler.class}),
	@Signature(type = StatementHandler.class, method = "save", args = Statement.class),
	@Signature(type = StatementHandler.class, method = "batch", args = Statement.class)
})
public class SqlLogInterceptor implements Interceptor {

	private static final String DRUID_POOLED_PREPARED_STATEMENT = "com.alibaba.druid.pool.DruidPooledPreparedStatement";

	private static volatile Method druidGetSqlMethod;

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		Statement statement;
		Object firstArg = invocation.getArgs()[0];
		if (Proxy.isProxyClass(firstArg.getClass())) {
			statement = (Statement) SystemMetaObject.forObject(firstArg).getValue("h.statement");
		} else {
			statement = (Statement) firstArg;
		}

		MetaObject metaObject = SystemMetaObject.forObject(statement);
		try {
			if (metaObject.hasGetter("delegate")) {
				statement = (Statement) metaObject.getValue("delegate");
			} else {
				statement = (Statement) metaObject.getValue("stmt.statement");
			}
		} catch (Exception ignored) {
		}

		String originalSql = null;
		String stmtClassName = statement.getClass().getName();
		if (DRUID_POOLED_PREPARED_STATEMENT.equals(stmtClassName)) {
			try {
				if (druidGetSqlMethod == null) {
					synchronized (SqlLogInterceptor.class) {
						if (druidGetSqlMethod == null) {
							Class<?> clazz = Class.forName(DRUID_POOLED_PREPARED_STATEMENT);
							druidGetSqlMethod = clazz.getMethod("getSql");
						}
					}
				}
				Object stmtSql = druidGetSqlMethod.invoke(statement);
				if (stmtSql instanceof String) {
					originalSql = (String) stmtSql;
				}
			} catch (Exception e) {
				log.error("Druid getSQL throw exception:{}", e.getMessage(), e);
			}
		}
		if (originalSql == null) {
			originalSql = statement.toString();
		}
		originalSql = originalSql.replaceAll("[\\s]+", StringPool.SPACE);
		int index = indexOfSqlStart(originalSql);
		if (index > 0) {
			originalSql = originalSql.substring(index);
		}

		long start = SystemClock.now();
		Object result = invocation.proceed();
		long timing = SystemClock.now() - start;
		Object target = PluginUtils.realTarget(invocation.getTarget());
		MetaObject metaObject1 = SystemMetaObject.forObject(target);
		MappedStatement ms = (MappedStatement) metaObject1.getValue("delegate.mappedStatement");
		String sqlLogger = "\n========== SQL Start ==========" +
			"\nExecute ID  : {}" +
			"\nExecute SQL : {}" +
			"\nExecute Time: {} ms" +
			"\n========== SQL End ==========";
		log.info(sqlLogger, ms.getId(), originalSql, timing);
		return result;
	}

	@Override
	public Object plugin(Object target) {
		if (target instanceof StatementHandler) {
			return Plugin.wrap(target, this);
		}
		return target;
	}

	private int indexOfSqlStart(String sql) {
		String upperCaseSql = sql.toUpperCase();
		Set<Integer> set = Sets.newHashSet();
		set.add(upperCaseSql.indexOf("SELECT "));
		set.add(upperCaseSql.indexOf("UPDATE "));
		set.add(upperCaseSql.indexOf("INSERT "));
		set.add(upperCaseSql.indexOf("DELETE "));
		set.remove(-1);
		if (CollectionUtils.isEmpty(set)) {
			return -1;
		}
		List<Integer> list = new ArrayList<>(set);
		list.sort(Comparator.naturalOrder());
		return list.get(0);
	}
}
