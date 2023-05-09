package org.ylzl.eden.spring.data.jdbc.datasource;

import org.ylzl.eden.extension.SPI;

import javax.sql.DataSource;

/**
 * 数据源解析器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@SPI
public interface DataSourceResolver {

	/**
	 * 解析数据源
	 *
	 * @param originalDataSource 原始数据源
	 * @return 解析后的数据源
	 */
	DataSource resolveDataSource(DataSource originalDataSource);
}
