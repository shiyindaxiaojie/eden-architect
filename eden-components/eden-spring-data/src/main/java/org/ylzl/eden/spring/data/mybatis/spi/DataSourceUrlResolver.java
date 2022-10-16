package org.ylzl.eden.spring.data.mybatis.spi;

import javax.sql.DataSource;

/**
 * 数据源地址解析器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public interface DataSourceUrlResolver {

	String getDataSourceUrl(DataSource dataSource);
}
