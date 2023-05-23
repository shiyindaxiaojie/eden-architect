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

package org.ylzl.eden.spring.integration.netty.rpc.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.ylzl.eden.spring.integration.netty.NettyServer;
import org.ylzl.eden.spring.integration.netty.rpc.RpcServer;
import org.ylzl.eden.spring.integration.netty.rpc.codec.RpcReadDecoder;
import org.ylzl.eden.spring.integration.netty.rpc.codec.RpcWriteEncoder;
import org.ylzl.eden.spring.integration.netty.rpc.model.RpcRequest;
import org.ylzl.eden.spring.integration.netty.rpc.model.RpcResponse;
import org.ylzl.eden.spring.integration.netty.rpc.serializer.Serializer;

/**
 * Netty RPC 服务端
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
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
