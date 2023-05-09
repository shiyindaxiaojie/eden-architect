package org.ylzl.eden.shardingsphere.spring.boot.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.driver.jdbc.context.JDBCContext;
import org.apache.shardingsphere.driver.jdbc.core.datasource.ShardingSphereDataSource;
import org.ylzl.eden.spring.data.jdbc.datasource.DataSourceUrlParser;
import org.ylzl.eden.spring.data.jdbc.datasource.DataSourceUrlParserException;

import javax.sql.DataSource;
import java.lang.reflect.Field;

/**
 * ShardingDataSource 数据源解析器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Slf4j
public class ShardingSphereDataSourceUrlParser implements DataSourceUrlParser {

	private static final String JDBC_CONTEXT = "jdbcContext";

	/**
	 * 获取数据源地址
	 *
	 * @param dataSource 数据源
	 * @return 数据源地址
	 */
	@Override
	public String getDataSourceUrl(DataSource dataSource) {
		if (ShardingSphereDataSource.class.isAssignableFrom(dataSource.getClass())) {
			try {
				Field field = dataSource.getClass().getDeclaredField(JDBC_CONTEXT);
				field.setAccessible(true);
				JDBCContext jdbcContext = (JDBCContext) field.get(dataSource);
				return jdbcContext.getCachedDatabaseMetaData().getUrl();
			} catch (Exception ex) {
				log.error(ex.getMessage(), ex);
				throw new DataSourceUrlParserException(ex.getMessage(), ex);
			}
		}
		return null;
	}
}
