package org.ylzl.eden.spring.integration.cat;

import lombok.experimental.UtilityClass;

/**
 * CAT 常量定义扩展，需要对齐 CAT 规则
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@UtilityClass
public class CatConstants extends com.dianping.cat.CatConstants {

	// HTTP
	// 参考 com.dianping.cat.consumer.matrix.MessageAnalyzer
	// 参考 com.dianping.cat.consumer.dependency.DependencyAnalyzer
	public static final String TYPE_URL = com.dianping.cat.CatConstants.TYPE_URL;
	public static final String TYPE_URL_SERVER = TYPE_URL + ".Server";
	public static final String TYPE_URL_METHOD = TYPE_URL + ".Method";
	public static final String TYPE_URL_HEADER = TYPE_URL + ".Header";
	public static final String TYPE_URL_BODY = TYPE_URL + ".Body";
	public static final String TYPE_URL_FORWARD = com.dianping.cat.CatConstants.TYPE_URL_FORWARD;
	public static final String TYPE_URL_FORWARD_METHOD = TYPE_URL_FORWARD + ".Method";
	public static final String TYPE_URL_FORWARD_HEADER = TYPE_URL_FORWARD + ".Header";
	public static final String TYPE_URL_FORWARD_BODY = TYPE_URL_FORWARD + ".Body";
	public final static String TYPE_URL_BIZ_ERROR = TYPE_URL + ".BizError";
	public final static String TYPE_URL_TIMEOUT_ERROR = TYPE_URL + ".TimeoutError";
	public final static String TYPE_URL_REDIRECTION_ERROR = TYPE_URL + ".RedirectionError";
	public final static String TYPE_URL_CLIENT_ERROR = TYPE_URL + ".ClientError";
	public final static String TYPE_URL_SERVER_ERROR = TYPE_URL + ".ServerError";

	// RPC
	// 参考：com.dianping.cat.consumer.storage.builder.StorageRPCBuilder
	// 参考：com.dianping.cat.consumer.dependency.DependencyAnalyzer
	public final static String TYPE_RPC_CALL = "RpcCall";
	public final static String TYPE_RPC_CALL_APP = TYPE_RPC_CALL + ".App";
	public final static String TYPE_RPC_CALL_HOST = TYPE_RPC_CALL + ".Host";
	public final static String TYPE_RPC_CALL_PORT = TYPE_RPC_CALL + ".Port";
	public final static String TYPE_RPC_CALL_BIZ_ERROR = TYPE_RPC_CALL + ".BizError";
	public final static String TYPE_RPC_CALL_TIMEOUT_ERROR = TYPE_RPC_CALL + ".TimeoutError";
	public final static String TYPE_RPC_CALL_REMOTING_ERROR = TYPE_RPC_CALL + ".RemotingError";

	public final static String TYPE_RPC_SERVICE = "RpcService";
	public final static String TYPE_RPC_SERVICE_APP = TYPE_RPC_SERVICE + ".App";
	public final static String TYPE_RPC_SERVICE_HOST = TYPE_RPC_SERVICE + ".Host";
	public final static String TYPE_RPC_SERVICE_PORT = TYPE_RPC_SERVICE + ".Port";
	public final static String TYPE_RPC_SERVICE_BIZ_ERROR = TYPE_RPC_SERVICE + ".BizError";
	public final static String TYPE_RPC_SERVICE_TIMEOUT_ERROR = TYPE_RPC_SERVICE + ".TimeoutError";
	public final static String TYPE_RPC_SERVICE_REMOTING_ERROR = TYPE_RPC_SERVICE + ".RemotingError";

	// SQL
	// 参考 com.dianping.cat.consumer.storage.builder.StorageSQLBuilder
	public static final String TYPE_SQL = com.dianping.cat.CatConstants.TYPE_SQL;
	public static final String TYPE_SQL_DATABASE = TYPE_SQL + ".Database";
	public static final String TYPE_SQL_METHOD = TYPE_SQL + ".Method";

	// Cache
	// 参考 com.dianping.cat.consumer.storage.builder.StorageCacheBuilder
	public static final String TYPE_CACHE = "Cache";
	public static final String TYPE_CACHE_REDIS = TYPE_CACHE + ".Redis";
	public static final String TYPE_CACHE_DATABASE = TYPE_CACHE + ".Database";

	// MQ
	public final static String TYPE_MQ_PRODUCER = "MQProducer";
	public final static String TYPE_MQ_PRODUCER_TOPIC = TYPE_MQ_PRODUCER + ".Topic";
	public final static String TYPE_MQ_PRODUCER_ADDR = TYPE_MQ_PRODUCER + ".Addr";
	public final static String TYPE_MQ_CONSUMER = "MQConsumer";
	public final static String TYPE_MQ_CONSUMER_TOPIC = TYPE_MQ_CONSUMER + ".Topic";
	public final static String TYPE_MQ_CONSUMER_ADDR = TYPE_MQ_CONSUMER + ".Addr";

	// Log
	public static final String TYPE_LOG_LOG4J2 = "Log4j2";

	// 附加数据
	public static final String DATA_VERSION = "version";
	public static final String DATA_COMPONENT = "component";
	public static final String DATA_PROTOCOL = "protocol";
	public static final String DATA_COMPONENT_DUBBO = "Dubbo";
	public static final String DATA_COMPONENT_REST_TEMPLATE = "RestTemplate";
	public static final String DATA_COMPONENT_FEIGN = "Feign";
	public static final String DATA_COMPONENT_MYBATIS = "Mybatis";
	public static final String DATA_COMPONENT_REDIS = "Redis";
	public static final String DATA_COMPONENT_CAFFEINE = "Caffeine";
	public static final String DATA_COMPONENT_GUAVA = "Guava";
	public static final String DATA_COMPONENT_KAFKA = "Kafka";
	public static final String DATA_COMPONENT_ROCKETMQ = "RocketMQ";

	// Trace
	// 自定义链路传递参数
	public static final String X_FORWARDED_FOR = "x-forwarded-for";
	public static final String X_CAT_TRACE_MODE = "x-cat-trace-mode";
	public static final String X_CAT_ID = "x-cat-id";
	public static final String X_CAT_CHILD_ID = "x-cat-child-id";
	public static final String X_CAT_PARENT_ID = "x-cat-parent-id";
	public static final String X_CAT_SERVER = "x-cat-server";

	// Rewrite CatFilter
	public static final String CAT_STATE = com.dianping.cat.CatConstants.CAT_STATE;
	public static final String CAT_PAGE_URI = com.dianping.cat.CatConstants.CAT_PAGE_URI;
	public static final String CAT_PAGE_TYPE = com.dianping.cat.CatConstants.CAT_PAGE_TYPE;
}
