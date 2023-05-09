package org.ylzl.eden.spring.data.jdbc.datasource;

/**
 * 数据源地址解析异常
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class DataSourceUrlParserException extends RuntimeException {

	public DataSourceUrlParserException(String message, Throwable ex) {
		super(message, ex);
	}
}
