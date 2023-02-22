package org.ylzl.eden.spring.integration.cat.extension;

import lombok.experimental.UtilityClass;

/**
 * CAT 常量定义扩展，需要对齐 CAT 规则
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@UtilityClass
public class CatConstants extends com.dianping.cat.CatConstants {

	public final static String TRACE_MODE = "X-CAT-TRACE-MODE";

	// Dubbo
	// 参考：com.dianping.cat.consumer.storage.builder.StorageRPCBuilder
	// 参考：com.dianping.cat.consumer.dependency.DependencyAnalyzer
	public final static String TYPE_PROVIDER = "PigeonService";
	public final static String TYPE_PROVIDER_APP = TYPE_PROVIDER + ".app";
	public final static String TYPE_PROVIDER_SERVER = TYPE_PROVIDER + ".server";
	public final static String TYPE_PROVIDER_BIZ_ERROR = TYPE_PROVIDER + ".biz.error";
	public final static String TYPE_PROVIDER_TIMEOUT_ERROR = TYPE_PROVIDER + ".timeout.error";
	public final static String TYPE_PROVIDER_REMOTING_ERROR = TYPE_PROVIDER + ".remoting.error";
	public final static String TYPE_CONSUMER = "PigeonCall";
	public final static String TYPE_CONSUMER_APP = TYPE_CONSUMER + ".app";
	public final static String TYPE_CONSUMER_SERVER = TYPE_CONSUMER + ".server";
	public final static String TYPE_CONSUMER_PORT = TYPE_CONSUMER + ".port";
	public final static String TYPE_CONSUMER_BIZ_ERROR = TYPE_PROVIDER + ".biz.error";
	public final static String TYPE_CONSUMER_TIMEOUT_ERROR = TYPE_PROVIDER + ".timeout.error";
	public final static String TYPE_CONSUMER_REMOTING_ERROR = TYPE_PROVIDER + ".remoting.error";

	// SQL
	// 参考 com.dianping.cat.consumer.storage.builder.StorageSQLBuilder
	public static final String TYPE_SQL = "SQL";
	public static final String TYPE_SQL_DATABASE = "SQL.Database";
	public static final String TYPE_SQL_METHOD = "SQL.Method";

	// Cache
	// 参考 com.dianping.cat.consumer.storage.builder.StorageCacheBuilder
	public static final String TYPE_CACHE = "Cache";
	public static final String TYPE_CACHE_SERVER = TYPE_CACHE + ".server";
	public static final String TYPE_CACHE_REDIS = TYPE_CACHE + ".Redis";
	public static final String TYPE_CACHE_CAFFEINE = TYPE_CACHE + ".Caffeine";
	public static final String TYPE_CACHE_GUAVA = TYPE_CACHE + ".Guava";

	// Http
	// 参考 com.dianping.cat.consumer.matrix.MessageAnalyzer
	// 参考 com.dianping.cat.consumer.dependency.DependencyAnalyzer
	public static final String TYPE_URL = "URL";
	public static final String TYPE_URL_FORWARD = TYPE_URL + ".Forward";
	public static final String TYPE_URL_SERVER = TYPE_URL + ".Server";
	public static final String TYPE_URL_METHOD = TYPE_URL + ".Method";

	// Trace
	// 自定义链路传递参数
	public static final String HTTP_HEADER_CHILD_MESSAGE_ID = "X-CAT-CHILD-MESSAGE-ID";
	public static final String HTTP_HEADER_PARENT_MESSAGE_ID = "X-CAT-PARENT-MESSAGE-ID";
	public static final String HTTP_HEADER_ROOT_MESSAGE_ID = "X-CAT-ROOT-MESSAGE-ID";
}
