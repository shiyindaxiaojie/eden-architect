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
