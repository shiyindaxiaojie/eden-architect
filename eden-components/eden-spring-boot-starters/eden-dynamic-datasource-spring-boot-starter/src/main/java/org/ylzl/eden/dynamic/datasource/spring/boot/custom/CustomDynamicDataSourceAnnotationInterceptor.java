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

package org.ylzl.eden.dynamic.datasource.spring.boot.custom;

import com.baomidou.dynamic.datasource.aop.DynamicDataSourceAnnotationInterceptor;
import com.baomidou.dynamic.datasource.processor.DsProcessor;
import com.baomidou.dynamic.datasource.support.DataSourceClassResolver;
import org.aopalliance.intercept.MethodInvocation;
import org.ylzl.eden.spring.data.jdbc.datasource.routing.RoutingDataSourceContextHolder;

/**
 * 自定义 DynamicDataSourceAnnotationInterceptor
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class CustomDynamicDataSourceAnnotationInterceptor extends DynamicDataSourceAnnotationInterceptor {

	private static final String DYNAMIC_PREFIX = "#";

	private final DataSourceClassResolver dataSourceClassResolver;

	private final DsProcessor dsProcessor;

	public CustomDynamicDataSourceAnnotationInterceptor(Boolean allowedPublicOnly, DsProcessor dsProcessor) {
		super(allowedPublicOnly, dsProcessor);
		this.dataSourceClassResolver = new DataSourceClassResolver(allowedPublicOnly);
		this.dsProcessor = dsProcessor;
	}

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		String dsKey = determineDatasourceKey(invocation);
		RoutingDataSourceContextHolder.push(dsKey);
		try {
			return invocation.proceed();
		} finally {
			RoutingDataSourceContextHolder.poll();
		}
	}

	private String determineDatasourceKey(MethodInvocation invocation) {
		String key = dataSourceClassResolver.findKey(invocation.getMethod(), invocation.getThis());
		return key.startsWith(DYNAMIC_PREFIX) ? dsProcessor.determineDatasource(invocation, key) : key;
	}
}
