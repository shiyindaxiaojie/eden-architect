package org.ylzl.eden.spring.boot.cat.plugins.mybatis.spi;

import javax.sql.DataSource;

/**
 * TODO
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public interface DataSourceUrlResolver {

	String getDataSourceUrl(DataSource dataSource);
}
