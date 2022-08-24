package org.ylzl.eden.spring.integration.cat.integration.mybatis.spi;

import com.zaxxer.hikari.HikariDataSource;
import org.ylzl.eden.commons.lang.StringUtils;

import javax.sql.DataSource;
import java.util.ServiceLoader;

/**
 * 数据源地址解释器支持类
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class DataSourceUrlResolverSupport {

/*	private static final String HIKARICP_CLASS_NAME = "com.zaxxer.hikari.HikariDataSource";

	private static final String DRUID_CLASS_NAME = "com.alibaba.druid.pool.DruidDataSource";*/

	public static String resolve(DataSource dataSource) {
		for (DataSourceUrlResolver adaptor : ServiceLoader.load(DataSourceUrlResolver.class)) {
			String url = adaptor.getDataSourceUrl(dataSource);
			if (StringUtils.isNotEmpty(url)) {
				return url;
			}
		}
		if (dataSource instanceof HikariDataSource) {
			return ((HikariDataSource) dataSource).getJdbcUrl();
		}
//		if (dataSource instanceof DruidDataSource) {
//			return ((DruidDataSource) dataSource).getUrl();
//		}
		return null;
	}


}
