package org.ylzl.eden.spring.integration.cat.integration.mybatis.spi;

import com.alibaba.druid.pool.DruidDataSource;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSource;
import org.ylzl.eden.commons.lang.StringUtils;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.util.ServiceLoader;

/**
 * 数据源地址解释器支持类
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class DataSourceUrlResolverSupport {

	private static final String HIKARICP_CLASS_NAME = "com.zaxxer.hikari.HikariDataSource";

	private static final String DRUID_CLASS_NAME = "com.alibaba.druid.pool.DruidDataSource";

	private static final String BAOMIDOU_CLASS_NAME = "com.baomidou.dynamic.datasource.DynamicRoutingDataSource";

	private static final String DATA_SOURCE = "dataSource";

	public static String parse(DataSource dataSource) throws NoSuchFieldException, IllegalAccessException {
		for (DataSourceUrlResolver adaptor : ServiceLoader.load(DataSourceUrlResolver.class)) {
			String url = adaptor.getDataSourceUrl(dataSource);
			if (StringUtils.isNotEmpty(url)) {
				return url;
			}
		}

		if (dataSource.getClass().getName().equalsIgnoreCase(BAOMIDOU_CLASS_NAME)) {
			dataSource = ((com.baomidou.dynamic.datasource.DynamicRoutingDataSource) dataSource).determineDataSource();
			if (dataSource instanceof com.baomidou.dynamic.datasource.ds.ItemDataSource) {
				dataSource = ((com.baomidou.dynamic.datasource.ds.ItemDataSource) dataSource).getRealDataSource();
			}
		}
		if (dataSource instanceof PooledDataSource) {
			Field ds = dataSource.getClass().getDeclaredField(DATA_SOURCE);
			ds.setAccessible(true);
			UnpooledDataSource uds = (UnpooledDataSource) ds.get(dataSource);
			return uds.getUrl();
		}
		if (dataSource.getClass().getName().equalsIgnoreCase(HIKARICP_CLASS_NAME)) {
			return ((HikariDataSource) dataSource).getJdbcUrl();
		}
		if (dataSource.getClass().getName().equalsIgnoreCase(DRUID_CLASS_NAME)) {
			return ((DruidDataSource) dataSource).getUrl();
		}
		return null;
	}
}
