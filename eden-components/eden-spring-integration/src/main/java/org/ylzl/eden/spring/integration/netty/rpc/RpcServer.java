package org.ylzl.eden.spring.integration.netty.rpc;

/**
 * RPC 服务端
 *
 * @author gyl
 * @since 2.0.0
 */
public interface RpcServer {

  void startup();

  void shutdown();
}
