package org.ylzl.eden.spring.data.jdbc.datasource.spi;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSource;

import javax.sql.DataSource;
import java.lang.reflect.Field;

/**
 * PooledDataSource 数据源解析器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Slf4j
public class MybatisDataSourceUrlParser implements DataSourceUrlParser {

	private static final String DATA_SOURCE = "dataSource";

	/**
	 * 获取数据源地址
	 *
	 * @param dataSource 数据源
	 * @return 数据源地址
	 */
	@Override
	public String getDataSourceUrl(DataSource dataSource) {
		if (dataSource instanceof PooledDataSource) {
			try {
				Field ds = dataSource.getClass().getDeclaredField(DATA_SOURCE);
				ds.setAccessible(true);
				UnpooledDataSource uds = (UnpooledDataSource) ds.get(dataSource);
				return uds.getUrl();
			} catch (Exception ex) {
				log.error(ex.getMessage(), ex);
				throw new DataSourceUrlParserException(ex.getMessage(), ex);
			}
		}
		return null;
	}
}
