package org.ylzl.eden.spring.boot.integration.netty.rpc.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.ylzl.eden.spring.boot.integration.netty.rpc.*;
import org.ylzl.eden.spring.boot.integration.netty.rpc.codec.RpcReadDecoder;
import org.ylzl.eden.spring.boot.integration.netty.rpc.codec.RpcWriteEncoder;
import org.ylzl.eden.spring.boot.integration.netty.rpc.serializer.Serializer;
import org.ylzl.eden.spring.boot.integration.netty.bootstrap.NettyServer;

/**
 * Netty RPC 服务端
 *
 * @author gyl
 * @since 2.0.0
 */
public class NettyRpcServer implements RpcServer {

  private NettyServer nettyServer;

  private Serializer serializer;

  public NettyRpcServer(String name, String host, int port, Serializer serializer) {
    this.nettyServer = new NettyServer(name, host, port);
    this.serializer = serializer;
  }

  @Override
  public void startup() {
    if (!nettyServer.isInitialized()) {
      nettyServer.addChannelHandler(
          new ChannelInitializer<SocketChannel>() {

            @Override
            protected void initChannel(SocketChannel ch) {
              ChannelPipeline pipeline = ch.pipeline();
              pipeline.addLast(new LengthFieldBasedFrameDecoder(65535, 0, 4));
              pipeline.addLast(new RpcWriteEncoder(RpcResponse.class, serializer));
              pipeline.addLast(new RpcReadDecoder(RpcRequest.class, serializer));
              pipeline.addLast(new NettyRpcServerHandler());
            }
          });
      nettyServer.startup();
    }
  }

  @Override
  public void shutdown() {
    nettyServer.shutdown();
  }
}
