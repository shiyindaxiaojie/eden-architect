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
import com.dianping.cat.message.Message;
import com.dianping.cat.message.Transaction;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.remoting.RemotingException;
import org.apache.dubbo.remoting.TimeoutException;
import org.apache.dubbo.rpc.*;
import org.slf4j.MDC;
import org.ylzl.eden.commons.lang.StringUtils;
import org.ylzl.eden.commons.lang.Strings;
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
		String type = isConsumerSide ? CatConstants.TYPE_RPC_CALL : CatConstants.TYPE_RPC_SERVICE;
		String name = invoker.getInterface().getSimpleName() + Strings.DOT + invocation.getMethodName();
		Transaction transaction = Cat.newTransaction(type, name);
		transaction.addData(CatConstants.DATA_COMPONENT, CatConstants.DATA_COMPONENT_DUBBO);
		transaction.addData(CatConstants.DATA_VERSION, url.getVersion());
		transaction.addData(CatConstants.DATA_PROTOCOL, url.getProtocol());

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
				this.logRpcErrorEvent(throwable, isConsumerSide, name, throwable.getMessage());
				transaction.setStatus(result.getException().getClass().getSimpleName());
			} else {
				transaction.setStatus(Message.SUCCESS);
			}
			return result;
		} catch (RuntimeException e) {
			this.logRpcErrorEvent(e, isConsumerSide, name, e.getMessage());
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

	private void logRpcErrorEvent(Throwable throwable, boolean isConsumerSide, String name,
								  String nameValuePairs) {
		if (isConsumerSide) {
			if (RpcException.class == throwable.getClass()) {
				Throwable caseBy = throwable.getCause();
				if (caseBy != null && caseBy.getClass() == TimeoutException.class) {
					Cat.logEvent(CatConstants.TYPE_RPC_CALL_TIMEOUT_ERROR, name, TIMEOUT, nameValuePairs);
				} else {
					Cat.logEvent(CatConstants.TYPE_RPC_CALL_REMOTING_ERROR, name, REMOTING_ERROR, nameValuePairs);
				}
			} else if (RemotingException.class.isAssignableFrom(throwable.getClass())) {
				Cat.logEvent(CatConstants.TYPE_RPC_CALL_REMOTING_ERROR, name, REMOTING_ERROR, nameValuePairs);
			} else {
				Cat.logEvent(CatConstants.TYPE_RPC_CALL_BIZ_ERROR, name, BIZ_ERROR, nameValuePairs);
			}
		} else {
			if (RpcException.class == throwable.getClass()) {
				Throwable caseBy = throwable.getCause();
				if (caseBy != null && caseBy.getClass() == TimeoutException.class) {
					Cat.logEvent(CatConstants.TYPE_RPC_SERVICE_TIMEOUT_ERROR, name, TIMEOUT, nameValuePairs);
				} else {
					Cat.logEvent(CatConstants.TYPE_RPC_SERVICE_REMOTING_ERROR, name, REMOTING_ERROR, nameValuePairs);
				}
			} else if (RemotingException.class.isAssignableFrom(throwable.getClass())) {
				Cat.logEvent(CatConstants.TYPE_RPC_SERVICE, CatConstants.TYPE_RPC_SERVICE_REMOTING_ERROR, REMOTING_ERROR, nameValuePairs);
			} else {
				Cat.logEvent(CatConstants.TYPE_RPC_SERVICE_BIZ_ERROR, name, BIZ_ERROR, nameValuePairs);
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
		String providerAppName = RegistryFactoryWrapper.getProviderAppName(url);
		Cat.logEvent(CatConstants.TYPE_RPC_CALL_APP, providerAppName);

		Cat.logEvent(CatConstants.TYPE_RPC_CALL_HOST, url.getHost());

		Cat.logEvent(CatConstants.TYPE_RPC_CALL_PORT, String.valueOf(url.getPort()));
	}

	private void addProviderEvent(URL url) {
		Cat.logEvent(CatConstants.TYPE_RPC_SERVICE_APP, getConsumerAppName());

		Cat.logEvent(CatConstants.TYPE_RPC_SERVICE_HOST, url.getHost());

		Cat.logEvent(CatConstants.TYPE_RPC_SERVICE_PORT, String.valueOf(url.getPort()));
	}

	private String getConsumerAppName() {
		String appName = RpcContext.getContext().getAttachment(CommonConstants.APPLICATION_KEY);
		if (StringUtils.isBlank(appName)) {
			appName = RpcContext.getContext().getRemoteHost() + Strings.COLON + RpcContext.getContext().getRemotePort();
		}
		return appName;
	}
}
