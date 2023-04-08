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

package org.ylzl.eden.spring.integration.cat.integration.mybatis;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Message;
import com.dianping.cat.message.Transaction;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.ylzl.eden.commons.lang.Strings;
import org.ylzl.eden.spring.data.mybatis.util.MybatisUtils;
import org.ylzl.eden.spring.integration.cat.CatConstants;

/**
 * Mybatis 集成 CAT 插件
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Intercepts({
	@Signature(method = "query", type = Executor.class, args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
	@Signature(method = "query", type = Executor.class, args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
	@Signature(method = "update", type = Executor.class, args = {MappedStatement.class, Object.class})
})
public class MybatisCatInterceptor implements Interceptor {

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
		String methodName = MybatisUtils.getMethodName(mappedStatement);

		Transaction transaction = Cat.newTransaction(CatConstants.TYPE_SQL, methodName);
		transaction.addData(CatConstants.DATA_COMPONENT, CatConstants.DATA_COMPONENT_MYBATIS);

		String dataSourceUrl = MybatisUtils.getDatabaseUrl(mappedStatement);
		if (dataSourceUrl != null && dataSourceUrl.contains(Strings.PLACEHOLDER)) {
			dataSourceUrl = dataSourceUrl.substring(0, dataSourceUrl.indexOf(Strings.PLACEHOLDER));
		}
		Cat.logEvent(CatConstants.TYPE_SQL_DATABASE, dataSourceUrl);

		String sql = MybatisUtils.getSql(mappedStatement, invocation);
		Cat.logEvent(CatConstants.TYPE_SQL_METHOD, mappedStatement.getSqlCommandType().name(), Message.SUCCESS, sql);
		try {
			Object returnValue = invocation.proceed();
			transaction.setStatus(Transaction.SUCCESS);
			return returnValue;
		} catch (Throwable e) {
			transaction.setStatus(e);
			throw e;
		} finally {
			transaction.complete();
		}
	}

	@Override
	public Object plugin(Object target) {
		if (target instanceof Executor) {
			return Plugin.wrap(target, this);
		}
		return target;
	}
}
