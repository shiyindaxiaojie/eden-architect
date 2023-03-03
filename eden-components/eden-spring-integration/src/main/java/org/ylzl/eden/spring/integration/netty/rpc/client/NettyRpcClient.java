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

package org.ylzl.eden.spring.integration.netty.rpc.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.SneakyThrows;
import org.ylzl.eden.spring.integration.netty.NettyClient;
import org.ylzl.eden.spring.integration.netty.rpc.RpcClient;
import org.ylzl.eden.spring.integration.netty.rpc.model.RpcRequest;
import org.ylzl.eden.spring.integration.netty.rpc.model.RpcResponse;
import org.ylzl.eden.spring.integration.netty.rpc.codec.RpcReadDecoder;
import org.ylzl.eden.spring.integration.netty.rpc.codec.RpcWriteEncoder;
import org.ylzl.eden.spring.integration.netty.rpc.serializer.Serializer;

/**
 * Netty RPC 客户端
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
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
