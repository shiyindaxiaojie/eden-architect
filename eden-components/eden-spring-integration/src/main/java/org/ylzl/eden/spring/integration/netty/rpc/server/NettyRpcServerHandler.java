/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ylzl.eden.spring.integration.netty.rpc.server;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;
import org.ylzl.eden.spring.integration.netty.rpc.RpcRequest;
import org.ylzl.eden.spring.integration.netty.rpc.RpcResponse;

import java.util.ServiceLoader;

/**
 * Netty RPC 服务端处理器
 *
 * @author gyl
 * @since 2.0.0
 */
@ChannelHandler.Sharable
public class NettyRpcServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

	@Override
	public void channelRead0(ChannelHandlerContext ctx, RpcRequest msg) throws Exception {
		RpcResponse rpcResponse = new RpcResponse();
		rpcResponse.setRequestId(msg.getRequestId());
		try {
			Object result = invoke(msg);
			rpcResponse.setResult(result);
		} catch (Throwable throwable) {
			rpcResponse.setThrowable(throwable);
			throwable.printStackTrace();
		}
		ctx.writeAndFlush(rpcResponse);
	}

	private Object invoke(RpcRequest request) throws Throwable {
		Class<?> clazz = Class.forName(request.getClassName());
		ServiceLoader<?> serviceProviders = ServiceLoader.load(clazz); // 这里用 SPI 实现，也可以切换为 Spring 管理
		Object serviceImplBean = serviceProviders.iterator().next();
		Class<?> serviceImplClass = serviceImplBean.getClass();
		FastClass fastClass = FastClass.create(serviceImplClass);

		String methodName = request.getMethodName();
		Class<?>[] parameterTypes = request.getParameterTypes();
		Object[] parameters = request.getParameters();
		FastMethod fastMethod = fastClass.getMethod(methodName, parameterTypes);
		return fastMethod.invoke(serviceImplBean, parameters);
	}
}
