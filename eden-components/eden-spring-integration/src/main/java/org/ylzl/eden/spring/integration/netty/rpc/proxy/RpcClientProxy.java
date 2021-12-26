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

package org.ylzl.eden.spring.integration.netty.rpc.proxy;

import net.sf.cglib.proxy.InvocationHandler;
import org.ylzl.eden.commons.bytecode.CglibProxy;
import org.ylzl.eden.spring.integration.netty.rpc.RpcClient;
import org.ylzl.eden.spring.integration.netty.rpc.RpcRequest;
import org.ylzl.eden.spring.integration.netty.rpc.RpcResponse;

import java.util.UUID;

/**
 * RPC 客户端代理
 *
 * @author gyl
 * @since 2.0.0
 */
public class RpcClientProxy {

  private final RpcClient rpcClient;

  public RpcClientProxy(RpcClient rpcClient) {
    this.rpcClient = rpcClient;
  }

  public <T> T newProxyInstance(Class<T> clazz, int timeout) {
    return CglibProxy.newProxyInstance(
        clazz,
        (InvocationHandler)
            (obj, method, args) -> {
              RpcRequest request = new RpcRequest();
              request.setRequestId(UUID.randomUUID().toString());
              request.setClassName(method.getDeclaringClass().getName());
              request.setMethodName(method.getName());
              request.setParameterTypes(method.getParameterTypes());
              request.setParameters(args);
              RpcResponse rpcResponse = rpcClient.invoke(request, timeout);
              if (rpcResponse.getThrowable() != null) {
                throw rpcResponse.getThrowable();
              }
              return rpcResponse.getResult();
            });
  }
}
