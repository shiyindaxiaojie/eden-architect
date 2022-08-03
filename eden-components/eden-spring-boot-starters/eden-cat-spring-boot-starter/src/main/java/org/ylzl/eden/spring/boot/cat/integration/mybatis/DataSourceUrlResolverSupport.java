package org.ylzl.eden.spring.boot.cat.integration.mybatis;

import org.ylzl.eden.commons.lang.StringUtils;
import org.ylzl.eden.spring.boot.cat.integration.mybatis.spi.DataSourceUrlResolver;

import javax.sql.DataSource;
import java.util.ServiceLoader;

/**
 * TODO
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class DataSourceUrlResolverSupport {

	public static String resolve(DataSource dataSource) {
		for (DataSourceUrlResolver adaptor : ServiceLoader.load(DataSourceUrlResolver.class)) {
			String url = adaptor.getDataSourceUrl(dataSource);
			if (StringUtils.isNotEmpty(url)) {
				return url;
			}
		}
		return null;
	}
}
