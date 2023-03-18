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

	private static final String IN_JVM = "injvm";

	private static final String BIZ_ERROR = "BIZ_ERROR";
	private static final String TIMEOUT = "TIMEOUT";
	private static final String REMOTING_ERROR = "REMOTING_ERROR";

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
			Cat.Context context = this.initContext();
			if (isConsumerSide) {
				this.addConsumerEvent(url);
				Cat.logRemoteCallClient(context, Cat.getManager().getDomain());
			} else {
				this.addProviderEvent(url);
				Cat.logRemoteCallServer(context);
			}
			this.setAttachment(context);

			result = invoker.invoke(invocation);
			if (result.hasException()) {
				Throwable throwable = result.getException();
				this.logRpcErrorEvent(throwable, isConsumerSide, name);
				transaction.setStatus(result.getException().getClass().getSimpleName());
			} else {
				transaction.setStatus(Message.SUCCESS);
			}
			return result;
		} catch (RuntimeException e) {
			this.logRpcErrorEvent(e, isConsumerSide, name);
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

	private void logRpcErrorEvent(Throwable throwable, boolean isConsumerSide, String nameValuePairs) {
		if (isConsumerSide) {
			if (RpcException.class == throwable.getClass()) {
				Throwable caseBy = throwable.getCause();
				if (caseBy != null && caseBy.getClass() == TimeoutException.class) {
					Cat.logEvent(CatConstants.TYPE_CONSUMER, CatConstants.TYPE_CONSUMER_TIMEOUT_ERROR,
						TIMEOUT, nameValuePairs);
				} else {
					Cat.logEvent(CatConstants.TYPE_CONSUMER, CatConstants.TYPE_CONSUMER_REMOTING_ERROR,
						REMOTING_ERROR, nameValuePairs);
				}
			} else if (RemotingException.class.isAssignableFrom(throwable.getClass())) {
				Cat.logEvent(CatConstants.TYPE_CONSUMER, CatConstants.TYPE_CONSUMER_REMOTING_ERROR,
					REMOTING_ERROR, nameValuePairs);
			} else {
				Cat.logEvent(CatConstants.TYPE_CONSUMER, CatConstants.TYPE_CONSUMER_BIZ_ERROR,
					BIZ_ERROR, nameValuePairs);
			}
		} else {
			if (RpcException.class == throwable.getClass()) {
				Throwable caseBy = throwable.getCause();
				if (caseBy != null && caseBy.getClass() == TimeoutException.class) {
					Cat.logEvent(CatConstants.TYPE_PROVIDER, CatConstants.TYPE_PROVIDER_TIMEOUT_ERROR,
						TIMEOUT, nameValuePairs);
				} else {
					Cat.logEvent(CatConstants.TYPE_PROVIDER, CatConstants.TYPE_PROVIDER_REMOTING_ERROR,
						REMOTING_ERROR, nameValuePairs);
				}
			} else if (RemotingException.class.isAssignableFrom(throwable.getClass())) {
				Cat.logEvent(CatConstants.TYPE_PROVIDER, CatConstants.TYPE_PROVIDER_REMOTING_ERROR,
					REMOTING_ERROR, nameValuePairs);
			} else {
				Cat.logEvent(CatConstants.TYPE_PROVIDER, CatConstants.TYPE_PROVIDER_BIZ_ERROR,
					BIZ_ERROR, nameValuePairs);
			}
		}
	}

	private Cat.Context initContext() {
		Cat.Context context = TraceContext.getContext();
		Map<String, String> attachments = RpcContext.getContext().getAttachments();
		if (attachments != null && attachments.size() > 0) {
			for (Map.Entry<String, String> entry : attachments.entrySet()) {
				if (CatConstants.X_CAT_ID.equals(entry.getKey())) {
					context.addProperty(Cat.Context.ROOT, entry.getValue());
				} else if (CatConstants.X_CAT_CHILD_ID.equals(entry.getKey())) {
					context.addProperty(Cat.Context.CHILD, entry.getValue());
				} else if (CatConstants.X_CAT_PARENT_ID.equals(entry.getKey())) {
					context.addProperty(Cat.Context.PARENT, entry.getValue());
				}
			}
		}
		return context;
	}

	private void setAttachment(Cat.Context context) {
		RpcContext.getContext().setAttachment(CatConstants.X_CAT_ID, context.getProperty(Cat.Context.ROOT));
		RpcContext.getContext().setAttachment(CatConstants.X_CAT_CHILD_ID, context.getProperty(Cat.Context.CHILD));
		RpcContext.getContext().setAttachment(CatConstants.X_CAT_PARENT_ID, context.getProperty(Cat.Context.PARENT));

		// 使用 Cat.Context.ROOT 作为链路ID
		MDC.put(TraceContext.TRACE_ID, TraceContext.getTraceId());
	}

	private void addConsumerEvent(URL url) {
		Cat.logEvent(CatConstants.TYPE_CONSUMER, CatConstants.TYPE_CONSUMER_APP,
			Event.SUCCESS, RegistryFactoryWrapper.getProviderAppName(url));

		Cat.logEvent(CatConstants.TYPE_CONSUMER, CatConstants.TYPE_CONSUMER_SERVER,
			Event.SUCCESS, url.getHost());

		Cat.logEvent(CatConstants.TYPE_CONSUMER, CatConstants.TYPE_CONSUMER_PORT,
			Event.SUCCESS, String.valueOf(url.getPort()));
	}

	private void addProviderEvent(URL url) {
		Cat.logEvent(CatConstants.TYPE_PROVIDER, CatConstants.TYPE_PROVIDER_APP,
			Event.SUCCESS, getConsumerAppName());

		Cat.logEvent(CatConstants.TYPE_PROVIDER, CatConstants.TYPE_PROVIDER_CLIENT,
			Event.SUCCESS, url.getHost());
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
}
