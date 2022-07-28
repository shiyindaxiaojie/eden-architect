package org.ylzl.eden.spring.boot.cat.constants;

import lombok.experimental.UtilityClass;

/**
 * TODO
 *
 * @author <a href="mailto:guoyuanlu@puyiwm.com">gyl</a>
 * @since 1.0.0
 */
@UtilityClass
public class CatConstants {

	public static final String LOG_TRACE_ID = "traceId";

	public static final String CAT_HTTP_HEADER_CHILD_MESSAGE_ID = "X-CAT-CHILD-MESSAGE-ID";

	public static final String CAT_HTTP_HEADER_PARENT_MESSAGE_ID = "X-CAT-PARENT-MESSAGE-ID";

	public static final String CAT_HTTP_HEADER_ROOT_MESSAGE_ID = "X-CAT-ROOT-MESSAGE-ID";

	public static final String CROSS_CONSUMER = "Call";

	public static final String CROSS_SERVER = "Service";

	public static final String CONSUMER_CALL_SERVER = "Call.server";

	public static final String CONSUMER_CALL_APP = "Call.app";

	public static final String CONSUMER_CALL_PORT = "Call.port";

	public static final String PROVIDER_CALL_SERVER = "Service.client";

	public static final String PROVIDER_SERVICE_APP = "Service.app";

	public static final String CLIENT_APPLICATION_NAME = "clientApplicationName";

	public static final String PROVIDER_APPLICATION_NAME = "serverApplicationName";

	public static final String TYPE_SQL = "SQL";

	public static final String TYPE_SQL_PARAM = "SQL.PARAM";

	public static final String TYPE_WEB = "WebRequest";

	public static final String HTTP_REQUEST = "HttpRequest";

	public static final String MQ_SEND = "MqSend";

	public static final String MQ_CONSUMER = "MqConsumer";

	public static final String MQ_MESSAGE = "MqMessage";

	public static final String MQ_HUGE_MESSAGE = "MqHugeMessage";

	public static final String MQ_SUCCESS_CONSUMER = "MqSuccessConsumer";

	public static final String MQ_FAILED_CONSUMER = "MqFailedConsumer";

	public static final String MQ_SUCCESS_SEND = "MqSuccessSend";

	public static final String MQ_FAILED_SEND = "MqFailedSend";

	public static final String METHOD_LOG = "MethodLog";

	public static final String METHOD_PARAM = "MethodParam";

	public static final String METHOD_RESULT = "MethodResult";

	public static final String METHOD_EXCEPTION_STACKTRACE = "MethodExceptionStackTrace";

	public static final String INNER_SERVICE = "InnerService";
}
