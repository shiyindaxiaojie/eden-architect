package org.ylzl.eden.spring.integration.netty.rpc.client;

import lombok.SneakyThrows;
import org.ylzl.eden.spring.integration.netty.rpc.RpcResponse;

/**
 * Netty RPC 异步调用结果
 *
 * @author gyl
 * @since 2.0.0
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
			lock.notify();
		}
	}
}
