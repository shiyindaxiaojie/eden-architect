/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
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

package org.ylzl.eden.spring.integration.netty.rpc.client;

import lombok.SneakyThrows;
import org.ylzl.eden.spring.integration.netty.rpc.model.RpcResponse;

/**
 * Netty RPC 异步调用结果
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class NettyRpcFuture {

	private final Object lock = new Object();
	private volatile boolean isSucceed = false;
	private RpcResponse rpcResponse;

	@SneakyThrows
	public RpcResponse get(int timeout) {
		synchronized (lock) {
			while (!isSucceed) {
				lock.wait(timeout);
			}
			return rpcResponse;
		}
	}

	public void set(RpcResponse response) {
		if (isSucceed) {
			return;
		}
		synchronized (lock) {
			this.rpcResponse = response;
			this.isSucceed = true;
			lock.notifyAll();
		}
	}
}
