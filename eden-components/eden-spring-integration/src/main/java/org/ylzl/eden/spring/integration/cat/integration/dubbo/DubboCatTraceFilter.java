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

package org.ylzl.eden.spring.integration.cat.integration.dubbo;

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
import org.slf4j.MDC;
import org.ylzl.eden.commons.lang.StringUtils;
import org.ylzl.eden.spring.integration.cat.CatConstants;
import org.ylzl.eden.spring.integration.cat.config.CatState;
import org.ylzl.eden.spring.integration.cat.integration.dubbo.registry.RegistryFactoryWrapper;
import org.ylzl.eden.spring.integration.cat.tracing.TraceContext;

import java.util.Map;

/**
 * Dubbo 链路集成 CAT 过滤
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Activate(group = {CommonConstants.PROVIDER, CommonConstants.CONSUMER}, order = -2)
public class DubboCatTraceFilter implements Filter {

	public static final String IN_JVM = "injvm";

	@Override
	public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
		if (!CatState.isInitialized()) {
			return invoker.invoke(invocation);
		}

		// 判断是否本地调用，防止消息树错乱
		URL url = invoker.getUrl();
		if (IN_JVM.equals(url.getProtocol())) {
			return invoker.invoke(invocation);
		}

		// 识别是消费方还是提供方
		String sideKey = url.getParameter(CommonConstants.SIDE_KEY);
		boolean isConsumerSide = CommonConstants.CONSUMER_SIDE.equals(sideKey);

		// 开启 Transaction
		String type = isConsumerSide ? CatConstants.TYPE_CONSUMER : CatConstants.TYPE_PROVIDER;
		String name = invoker.getInterface().getSimpleName() + "." + invocation.getMethodName();
		Transaction transaction = Cat.newTransaction(type, name);
		Result result = null;
		try {
			Cat.Context context = initContext();
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
						event = Cat.newEvent(isConsumerSide?
							CatConstants.TYPE_CONSUMER_TIMEOUT_ERROR : CatConstants.TYPE_PROVIDER_TIMEOUT_ERROR, name);
					} else {
						event = Cat.newEvent(isConsumerSide?
							CatConstants.TYPE_CONSUMER_REMOTING_ERROR : CatConstants.TYPE_PROVIDER_REMOTING_ERROR, name);
					}
				} else if (RemotingException.class.isAssignableFrom(throwable.getClass())) {
					event = Cat.newEvent(isConsumerSide?
						CatConstants.TYPE_CONSUMER_REMOTING_ERROR : CatConstants.TYPE_PROVIDER_REMOTING_ERROR, name);
				} else {
					event = Cat.newEvent(isConsumerSide?
						CatConstants.TYPE_CONSUMER_BIZ_ERROR : CatConstants.TYPE_PROVIDER_BIZ_ERROR, name);
				}
				event.setStatus(throwable);
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
					event = Cat.newEvent(isConsumerSide?
						CatConstants.TYPE_CONSUMER_TIMEOUT_ERROR : CatConstants.TYPE_PROVIDER_TIMEOUT_ERROR, name);
				} else {
					event = Cat.newEvent(isConsumerSide?
						CatConstants.TYPE_CONSUMER_REMOTING_ERROR : CatConstants.TYPE_PROVIDER_REMOTING_ERROR, name);
				}
			} else if (RemotingException.class.isAssignableFrom(e.getClass())) {
				event = Cat.newEvent(isConsumerSide?
					CatConstants.TYPE_CONSUMER_REMOTING_ERROR : CatConstants.TYPE_PROVIDER_REMOTING_ERROR, name);
			} else {
				event = Cat.newEvent(isConsumerSide?
					CatConstants.TYPE_CONSUMER_BIZ_ERROR : CatConstants.TYPE_PROVIDER_BIZ_ERROR, name);
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
			TraceContext.remove();
		}
	}

	private Cat.Context initContext() {
		Cat.Context context = TraceContext.getContext();
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
		Event appEvent = Cat.newEvent(CatConstants.TYPE_CONSUMER_APP, RegistryFactoryWrapper.getProviderAppName(url));
		appEvent.setStatus(Event.SUCCESS);
		completeEvent(appEvent);
		transaction.addChild(appEvent);

		Event hostEvent = Cat.newEvent(CatConstants.TYPE_CONSUMER_SERVER, url.getHost());
		hostEvent.setStatus(Event.SUCCESS);
		completeEvent(hostEvent);
		transaction.addChild(hostEvent);

		Event portEvent = Cat.newEvent(CatConstants.TYPE_CONSUMER_PORT, String.valueOf(url.getPort()));
		portEvent.setStatus(Event.SUCCESS);
		completeEvent(portEvent);
		transaction.addChild(portEvent);
	}

	private void addProviderEvent(URL url, Transaction transaction) {
		Event appEvent = Cat.newEvent(CatConstants.TYPE_PROVIDER_APP, getConsumerAppName());
		appEvent.setStatus(Event.SUCCESS);
		completeEvent(appEvent);
		transaction.addChild(appEvent);

		Event hostEvent = Cat.newEvent(CatConstants.TYPE_PROVIDER_CLIENT, url.getHost());
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

	private void setAttachment(Cat.Context context) {
		RpcContext.getContext().setAttachment(Cat.Context.ROOT, context.getProperty(Cat.Context.ROOT));
		RpcContext.getContext().setAttachment(Cat.Context.CHILD, context.getProperty(Cat.Context.CHILD));
		RpcContext.getContext().setAttachment(Cat.Context.PARENT, context.getProperty(Cat.Context.PARENT));

		// 使用 Cat.Context.ROOT 作为链路ID
		MDC.put(TraceContext.TRACE_ID, TraceContext.getTraceId());
	}

	private void completeEvent(Event event) {
		AbstractMessage message = (AbstractMessage) event;
		message.setCompleted(true);
	}
}
