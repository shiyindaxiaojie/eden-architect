package org.ylzl.eden.spring.integration.netty.rpc.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.SneakyThrows;
import org.ylzl.eden.spring.integration.netty.bootstrap.NettyClient;
import org.ylzl.eden.spring.integration.netty.rpc.RpcClient;
import org.ylzl.eden.spring.integration.netty.rpc.RpcRequest;
import org.ylzl.eden.spring.integration.netty.rpc.RpcResponse;
import org.ylzl.eden.spring.integration.netty.rpc.codec.RpcReadDecoder;
import org.ylzl.eden.spring.integration.netty.rpc.codec.RpcWriteEncoder;
import org.ylzl.eden.spring.integration.netty.rpc.serializer.Serializer;

/**
 * Netty RPC 客户端
 *
 * @author gyl
 * @since 2.0.0
 */
public class NettyRpcClient implements RpcClient {

  private NettyClient nettyClient;

  private NettyRpcClientHandler nettyRpcClientHandler;

  private Serializer serializer;

  public NettyRpcClient(String name, String host, int port, Serializer serializer) {
    this.nettyClient = new NettyClient(name, host, port);
    this.nettyRpcClientHandler = new NettyRpcClientHandler();
    this.serializer = serializer;
  }

  @Override
  public void startup() {
    if (!nettyClient.isInitialized()) {
      nettyClient.getChannelOptions().setSoKeepAlive(true);
      nettyClient.getChannelOptions().setTcpNodelay(true);
      nettyClient.addChannelHandler(
          new ChannelInitializer<SocketChannel>() {

            @Override
            protected void initChannel(SocketChannel ch) {
              ChannelPipeline pipeline = ch.pipeline();
              pipeline.addLast(new LengthFieldBasedFrameDecoder(65535, 0, 4));
              pipeline.addLast(new RpcWriteEncoder(RpcRequest.class, serializer));
              pipeline.addLast(new RpcReadDecoder(RpcResponse.class, serializer));
              pipeline.addLast(nettyRpcClientHandler);
            }
          });
      nettyClient.startup();
    }
  }

  @Override
  public void shutdown() {
    nettyClient.shutdown();
  }

  @SneakyThrows
  @Override
  public RpcResponse invoke(RpcRequest request, int timeout) {
    nettyClient.getChannel().writeAndFlush(request).await();
    return nettyRpcClientHandler.get(request.getRequestId(), timeout);
  }
}
