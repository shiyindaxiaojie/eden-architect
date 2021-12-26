package org.ylzl.eden.spring.integration.netty.rpc;

/**
 * RPC 客户端
 *
 * @author gyl
 * @since 2.0.0
 */
public interface RpcClient {

  void startup();

  void shutdown();

  RpcResponse invoke(RpcRequest request, int timeout);
}
