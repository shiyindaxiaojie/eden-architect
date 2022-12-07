package org.ylzl.eden.dynamic.datasource.spring.boot.custom;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import org.ylzl.eden.spring.data.jdbc.datasource.routing.RoutingDataSourceContextHolder;

import javax.sql.DataSource;

/**
 * 自定义 DynamicRoutingDataSource
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class CustomDynamicRoutingDataSource extends DynamicRoutingDataSource {

	@Override
	public DataSource determineDataSource() {
		String dsKey = RoutingDataSourceContextHolder.peek();
		return getDataSource(dsKey);
	}
}
