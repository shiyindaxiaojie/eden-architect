package org.ylzl.eden.spring.data.mybatis.plugin;

import com.baomidou.mybatisplus.core.toolkit.SystemClock;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.ylzl.eden.spring.data.mybatis.util.MybatisUtils;

/**
 * SQL 日志拦截器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Slf4j
@Intercepts({
	@Signature(method = "query", type = Executor.class, args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
	@Signature(method = "query", type = Executor.class, args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
	@Signature(method = "update", type = Executor.class, args = {MappedStatement.class, Object.class})
})
public class MybatisSqlLogInterceptor implements Interceptor {

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
		String originalSql = MybatisUtils.getSql(mappedStatement, invocation);
		long start = SystemClock.now();
		Object result = invocation.proceed();
		long timing = SystemClock.now() - start;
		String sqlLogger =
			"\nExecute ID  : {}" +
			"\nExecute SQL : {}" +
			"\nExecute Time: {} ms";
		log.info(sqlLogger, mappedStatement.getId(), originalSql, timing);
		return result;
	}

	@Override
	public Object plugin(Object target) {
		if (target instanceof Executor) {
			return Plugin.wrap(target, this);
		}
		return target;
	}
}
