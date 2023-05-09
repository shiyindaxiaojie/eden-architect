package org.ylzl.eden.dynamic.datasource.spring.boot.jdbc;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.ds.ItemDataSource;
import org.ylzl.eden.spring.data.jdbc.datasource.DataSourceResolver;

import javax.sql.DataSource;

/**
 * 动态数据源解析器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class DynamicDataSourceResolver implements DataSourceResolver {

	/**
	 * 解析数据源
	 *
	 * @param dataSource 原始数据源
	 * @return 解析后的数据源
	 */
	@Override
	public DataSource resolveDataSource(DataSource dataSource) {
		if (DynamicRoutingDataSource.class.isAssignableFrom(dataSource.getClass())) {
			dataSource = ((DynamicRoutingDataSource) dataSource).determineDataSource();
			if (dataSource instanceof ItemDataSource) {
				dataSource = ((ItemDataSource) dataSource).getRealDataSource();
			}
		}
		return dataSource;
	}
}
