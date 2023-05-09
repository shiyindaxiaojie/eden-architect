package org.ylzl.eden.spring.data.jdbc.datasource.spi;

import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

/**
 * HikariDataSource 数据源解析器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class HikariDataSourceUrlParser implements DataSourceUrlParser {

	private static final String CLASS_NAME = "com.zaxxer.hikari.HikariDataSource";

	/**
	 * 获取数据源地址
	 *
	 * @param dataSource 数据源
	 * @return 数据源地址
	 */
	@Override
	public String getDataSourceUrl(DataSource dataSource) {
		if (CLASS_NAME.equalsIgnoreCase(dataSource.getClass().getName())) {
			return ((HikariDataSource) dataSource).getJdbcUrl();
		}
		return null;
	}
}
