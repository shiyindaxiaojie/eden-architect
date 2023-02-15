/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ylzl.eden.spring.integration.cat.integration.dubbo.filter;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Event;
import com.dianping.cat.message.Message;
import com.dianping.cat.message.Transaction;
import com.dianping.cat.message.internal.AbstractMessage;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.remoting.RemotingException;
import org.apache.dubbo.remoting.TimeoutException;
import org.apache.dubbo.rpc.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.MDC;
import org.ylzl.eden.commons.lang.StringUtils;
import org.ylzl.eden.spring.integration.cat.integration.dubbo.registry.CatRegistryFactoryWrapper;
import org.ylzl.eden.spring.integration.cat.tracing.TraceContext;
import org.ylzl.eden.spring.integration.cat.integration.dubbo.EnableCatDubbo;

import java.util.Map;
import java.util.UUID;

/**
 * Dubbo 链路集成 CAT 过滤
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Activate(group = {CommonConstants.PROVIDER, CommonConstants.CONSUMER}, value = "cat", order = -2)
public class CatDubboTraceFilter implements Filter {

	private final static String TYPE_PROVIDER = "Dubbo.provider";

	private final static String TYPE_CONSUMER = "Dubbo.consumer";

	private final static String TYPE_PROVIDER_SERVICE_APP = "Dubbo.provider.service.app";

	private final static String TYPE_PROVIDER_SERVICE_HOST = "Dubbo.provider.service.host";

	private final static String TYPE_CONSUMER_CALL_APP = "Dubbo.consumer.call.app";

	private final static String TYPE_CONSUMER_CALL_HOST = "Dubbo.consumer.call.host";

	private final static String TYPE_CONSUMER_CALL_PORT = "Dubbo.consumer.call.port";

	private final static String TYPE_BIZ_ERROR = "Dubbo.biz.error";

	private final static String TYPE_TIMEOUT_ERROR = "Dubbo.timeout.error";

	private final static String TYPE_REMOTING_ERROR = "Dubbo.remoting.error";

	@Override
	public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
		if (!EnableCatDubbo.isEnabled()) {
			return invoker.invoke(invocation);
		}

		URL url = invoker.getUrl();
		String sideKey = url.getParameter(CommonConstants.SIDE_KEY);
		boolean isConsumerSide = CommonConstants.CONSUMER_SIDE.equals(sideKey);
		String type = isConsumerSide ? TYPE_CONSUMER : TYPE_PROVIDER;
		String name = invoker.getInterface().getSimpleName() + "." + invocation.getMethodName();
		Transaction transaction = Cat.newTransaction(type, name);

		String uuid = generateUUID();
		Result result = null;
		try {
			Cat.Context context = initContext(uuid);
			if (isConsumerSide) {
				addConsumerEvent(url, transaction);
				Cat.logRemoteCallClient(context, Cat.getManager().getDomain());
			} else {
				addProviderEvent(url, transaction);
				Cat.logRemoteCallServer(context);
			}
			setAttachment(context);

			result = invoker.invoke(invocation);
			if (result.hasException()) {
				Throwable throwable = result.getException();
				Event event;
				if (RpcException.class == throwable.getClass()) {
					Throwable caseBy = throwable.getCause();
					if (caseBy != null && caseBy.getClass() == TimeoutException.class) {
						event = Cat.newEvent(TYPE_TIMEOUT_ERROR, name);
					} else {
						event = Cat.newEvent(TYPE_REMOTING_ERROR, name);
					}
				} else if (RemotingException.class.isAssignableFrom(throwable.getClass())) {
					event = Cat.newEvent(TYPE_REMOTING_ERROR, name);
				} else {
					event = Cat.newEvent(TYPE_BIZ_ERROR, name);
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
			Event event;
			if (RpcException.class == e.getClass()) {
				Throwable caseBy = e.getCause();
				if (caseBy != null && caseBy.getClass() == TimeoutException.class) {
					event = Cat.newEvent(TYPE_TIMEOUT_ERROR, name);
				} else {
					event = Cat.newEvent(TYPE_REMOTING_ERROR, name);
				}
			} else {
				event = Cat.newEvent(TYPE_BIZ_ERROR, name);
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
			if (isConsumerSide) {
				TraceContext.remove(uuid);
			} else {
				TraceContext.remove();
			}
		}
	}

	@NotNull
	private static String generateUUID() {
		return UUID.randomUUID().toString();
	}

	private Cat.Context initContext(String uuid) {
		Cat.Context context = TraceContext.getContext(uuid);
		Map<String, String> attachments = RpcContext.getContext().getAttachments();
		if (attachments != null && attachments.size() > 0) {
			for (Map.Entry<String, String> entry : attachments.entrySet()) {
				if (Cat.Context.CHILD.equals(entry.getKey()) ||
					Cat.Context.ROOT.equals(entry.getKey()) ||
					Cat.Context.PARENT.equals(entry.getKey())) {
					context.addProperty(entry.getKey(), entry.getValue());
				}
			}
		}
		return context;
	}

	private void addConsumerEvent(URL url, Transaction transaction) {
		Event appEvent = Cat.newEvent(TYPE_CONSUMER_CALL_APP, CatRegistryFactoryWrapper.getProviderAppName(url));
		appEvent.setStatus(Event.SUCCESS);
		completeEvent(appEvent);
		transaction.addChild(appEvent);

		Event hostEvent = Cat.newEvent(TYPE_CONSUMER_CALL_HOST, url.getHost());
		hostEvent.setStatus(Event.SUCCESS);
		completeEvent(hostEvent);
		transaction.addChild(hostEvent);

		Event portEvent = Cat.newEvent(TYPE_CONSUMER_CALL_PORT, String.valueOf(url.getPort()));
		portEvent.setStatus(Event.SUCCESS);
		completeEvent(portEvent);
		transaction.addChild(portEvent);
	}

	private void addProviderEvent(URL url, Transaction transaction) {
		Event appEvent = Cat.newEvent(TYPE_PROVIDER_SERVICE_APP, getConsumerAppName());
		appEvent.setStatus(Event.SUCCESS);
		completeEvent(appEvent);
		transaction.addChild(appEvent);

		Event hostEvent = Cat.newEvent(TYPE_PROVIDER_SERVICE_HOST, url.getHost());
		hostEvent.setStatus(Event.SUCCESS);
		completeEvent(hostEvent);
		transaction.addChild(hostEvent);
	}

	private String getConsumerAppName() {
		String appName = RpcContext.getContext().getAttachment(CommonConstants.APPLICATION_KEY);
		if (StringUtils.isBlank(appName)) {
			appName = RpcContext.getContext().getRemoteHost() + ":" + RpcContext.getContext().getRemotePort();
		}
		return appName;
	}

	private void completeEvent(Event event) {
		AbstractMessage message = (AbstractMessage) event;
		message.setCompleted(true);
	}

	private void setAttachment(Cat.Context context) {
		RpcContext.getContext().setAttachment(Cat.Context.ROOT, context.getProperty(Cat.Context.ROOT));
		RpcContext.getContext().setAttachment(Cat.Context.CHILD, context.getProperty(Cat.Context.CHILD));
		RpcContext.getContext().setAttachment(Cat.Context.PARENT, context.getProperty(Cat.Context.PARENT));
		MDC.put(TraceContext.TRACE_ID, context.getProperty(Cat.Context.ROOT));
	}
}
