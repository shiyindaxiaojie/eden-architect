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

package org.ylzl.eden.spring.cloud.dubbo.filter;

import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.common.logger.Logger;
import org.apache.dubbo.common.logger.LoggerFactory;
import org.apache.dubbo.common.utils.ReflectUtils;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.rpc.*;
import org.apache.dubbo.rpc.service.GenericService;
import org.ylzl.eden.spring.framework.error.BaseException;

import java.lang.reflect.Method;

/**
 * Dubbo 自定义异常过滤器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Activate(group = CommonConstants.PROVIDER, order = -1)
public class DubboExceptionFilter implements Filter, Filter.Listener {

	private static final Logger logger = LoggerFactory.getLogger(DubboExceptionFilter.class);

	public DubboExceptionFilter() {
		logger.info("Dubbo3 exception filter initialized");
	}

	@Override
	public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
		return invoker.invoke(invocation);
	}

	@Override
	public void onResponse(Result appResponse, Invoker<?> invoker, Invocation invocation) {
		if (appResponse.hasException() && GenericService.class != invoker.getInterface()) {
			try {
				Throwable exception = appResponse.getException();

				// directly throw if it's checked exception
				if (!(exception instanceof RuntimeException) && (exception instanceof Exception)) {
					return;
				}
				// directly throw if the exception appears in the signature
				try {
					Method method = invoker.getInterface().getMethod(invocation.getMethodName(), invocation.getParameterTypes());
					Class<?>[] exceptionClasses = method.getExceptionTypes();
					for (Class<?> exceptionClass : exceptionClasses) {
						if (exception.getClass().equals(exceptionClass)) {
							return;
						}
					}
				} catch (NoSuchMethodException e) {
					return;
				}

				// for the exception not found in method's signature, print ERROR message in server's log.
				logger.error("Got unchecked and undeclared exception which called by " + RpcContext.getServiceContext().getRemoteHost() + ". service: " + invoker.getInterface().getName() + ", method: " + invocation.getMethodName() + ", exception: " + exception.getClass().getName() + ": " + exception.getMessage(), exception);

				// directly throw if exception class and interface class are in the same jar file.
				String serviceFile = ReflectUtils.getCodeBase(invoker.getInterface());
				String exceptionFile = ReflectUtils.getCodeBase(exception.getClass());
				if (serviceFile == null || exceptionFile == null || serviceFile.equals(exceptionFile)) {
					return;
				}
				// directly throw if it's JDK exception
				String className = exception.getClass().getName();
				if (className.startsWith("java.") || className.startsWith("javax.")) {
					return;
				}
				// directly throw if it's custom exception
				if (exception instanceof BaseException) {
					return;
				}
				// directly throw if it's dubbo exception
				if (exception instanceof RpcException) {
					return;
				}

				// otherwise, wrap with RuntimeException and throw back to the client
				appResponse.setException(new RuntimeException(StringUtils.toString(exception)));
			} catch (Throwable e) {
				logger.warn("Fail to ExceptionFilter when called by " + RpcContext.getServiceContext().getRemoteHost() + ". service: " + invoker.getInterface().getName() + ", method: " + invocation.getMethodName() + ", exception: " + e.getClass().getName() + ": " + e.getMessage(), e);
			}
		}
	}

	@Override
	public void onError(Throwable e, Invoker<?> invoker, Invocation invocation) {
		logger.error("Got unchecked and undeclared exception which called by " + RpcContext.getServiceContext().getRemoteHost() + ". service: " + invoker.getInterface().getName() + ", method: " + invocation.getMethodName() + ", exception: " + e.getClass().getName() + ": " + e.getMessage(), e);
	}
}
