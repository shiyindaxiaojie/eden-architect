package org.ylzl.eden.spring.boot.cat.integration.dubbo;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Event;
import com.dianping.cat.message.Message;
import com.dianping.cat.message.Transaction;
import com.dianping.cat.message.context.TraceContext;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.ylzl.eden.spring.boot.cat.integration.dubbo.registry.CatRegistryFactory;

import java.util.Map;
import java.util.UUID;

/**
 * TODO
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Activate(group = {CommonConstants.PROVIDER, CommonConstants.CONSUMER}, order = -9000)
public class DubboTraceFilter implements Filter {

	private final static String TYPE_PROVIDER = "Dubbo.Provider";

	private final static String TYPE_CONSUMER = "Dubbo.Consumer";

	private final static String DUBBO_BIZ_ERROR = "DUBBO_BIZ_ERROR";

	private final static String DUBBO_TIMEOUT_ERROR = "DUBBO_TIMEOUT_ERROR";

	private final static String DUBBO_REMOTING_ERROR = "DUBBO_REMOTING_ERROR";

	@Override
	public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
		if (!DubboCat.isEnable()) {
			return invoker.invoke(invocation);
		}

		URL url = invoker.getUrl();
		String sideKey = url.getParameter(CommonConstants.SIDE_KEY);
		String type = CommonConstants.PROVIDER_SIDE.equals(sideKey) ? TYPE_PROVIDER : TYPE_CONSUMER;
		String name = invoker.getInterface().getSimpleName() + "." + invocation.getMethodName();
		Transaction transaction = Cat.newTransaction(type, name);

		String uuid = UUID.randomUUID().toString();

		Result result = null;

		try {
			Cat.Context context = initContext(uuid);
			if (Constants.CONSUMER_SIDE.equals(sideKey)) {
				createConsumerCross(url, transaction);
				Cat.logRemoteCallClient(context);
			} else {
				createProviderCross(url, transaction);
				Cat.logRemoteCallServer(context);
			}
			setAttachment(context);
			result = invoker.invoke(invocation);

			if (result.hasException()) {
				//给调用接口出现异常进行打点
				Throwable throwable = result.getException();
				Event event = null;
				if (RpcException.class == throwable.getClass()) {
					Throwable caseBy = throwable.getCause();
					if (caseBy != null && caseBy.getClass() == TimeoutException.class) {
						event = Cat.newEvent(DUBBO_TIMEOUT_ERROR, loggerName);
					} else {
						event = Cat.newEvent(DUBBO_REMOTING_ERROR, loggerName);
					}
				} else if (RemotingException.class.isAssignableFrom(throwable.getClass())) {
					event = Cat.newEvent(DUBBO_REMOTING_ERROR, loggerName);
				} else {
					event = Cat.newEvent(DUBBO_BIZ_ERROR, loggerName);
				}
				event.setStatus(result.getException());
				completeEvent(event);
				transaction.addChild(event);
				transaction.setStatus(result.getException().getClass().getSimpleName());
			} else {
				transaction.setStatus(Message.SUCCESS);
			}
			return result;
		} catch (RuntimeException e) {
			Event event = null;
			if (RpcException.class == e.getClass()) {
				Throwable caseBy = e.getCause();
				if (caseBy != null && caseBy.getClass() == TimeoutException.class) {
					event = Cat.newEvent(DUBBO_TIMEOUT_ERROR, loggerName);
				} else {
					event = Cat.newEvent(DUBBO_REMOTING_ERROR, loggerName);
				}
			} else {
				event = Cat.newEvent(DUBBO_BIZ_ERROR, loggerName);
			}
			event.setStatus(e);
			completeEvent(event);
			transaction.addChild(event);
			transaction.setStatus(e.getClass().getSimpleName());
			if (result == null) {
				throw e;
			} else {
				return result;
			}
		} finally {
			transaction.complete();
			if (Constants.CONSUMER_SIDE.equals(sideKey)) {
				TraceContext.remove(uuid);
			} else {
				TraceContext.remove();
			}
		}
	}

	private Cat.Context initContext(String uuid) {
		Cat.Context context = TraceContext.getContext(uuid);
		Map<String, String> attachments = RpcContext.getContext().getAttachments();
		if (attachments != null && attachments.size() > 0) {
			for (Map.Entry<String, String> entry : attachments.entrySet()) {
				if (Cat.Context.CHILD.equals(entry.getKey()) || Cat.Context.ROOT.equals(entry.getKey()) || Cat.Context.PARENT.equals(entry.getKey())) {
					context.addProperty(entry.getKey(), entry.getValue());
				}
			}
		}
		return context;
	}

	public static final String CROSS_CONSUMER = "Call";

	public static final String CROSS_SERVER = "Service";

	public static final String CONSUMER_CALL_SERVER = "Call.server";

	public static final String CONSUMER_CALL_APP = "Call.app";

	public static final String CONSUMER_CALL_PORT = "Call.port";

	public static final String PROVIDER_CALL_SERVER = "Service.client";

	public static final String PROVIDER_SERVICE_APP = "Service.app";

	public static final String CLIENT_APPLICATION_NAME = "clientApplicationName";

	public static final String PROVIDER_APPLICATION_NAME = "serverApplicationName";

	private final static String TYPE_CONSUMER_CALL_APP_NAME = "Dubbo.Consumer.Call.AppName";

	private final static String TYPE_CONSUMER_CALL_HOST = "Dubbo.Consumer.Call.Host";

	private final static String TYPE_CONSUMER_CALL_PORT = "Dubbo.Consumer.Call.Port";

	private void createConsumerCross(URL url, Transaction transaction) {
		Event appEvent = Cat.newEvent(TYPE_CONSUMER_CALL_APP_NAME, CatRegistryFactory.getProviderAppName(url));
		appEvent.setStatus(Event.SUCCESS);
		appEvent.complete();
		transaction.addChild(appEvent);

		Event hostEvent = Cat.newEvent(TYPE_CONSUMER_CALL_HOST, url.getHost());
		hostEvent.setStatus(Event.SUCCESS);
		hostEvent.complete();
		transaction.addChild(hostEvent);

		Event portEvent = Cat.newEvent(TYPE_CONSUMER_CALL_PORT, String.valueOf(url.getPort()));
		portEvent.setStatus(Event.SUCCESS);
		portEvent.complete();
		transaction.addChild(portEvent);
	}
}
