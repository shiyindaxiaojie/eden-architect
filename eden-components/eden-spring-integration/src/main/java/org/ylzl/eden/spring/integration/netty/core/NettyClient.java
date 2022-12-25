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

package org.ylzl.eden.spring.integration.netty.core;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.ylzl.eden.spring.integration.netty.channel.ChannelOptions;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Netty 客户端
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@SuppressWarnings("unchecked")
@Slf4j
public class NettyClient {

	private final AtomicBoolean initialized = new AtomicBoolean(false);

	private final String name;

	private final String host;

	private final int port;

	private final List<ChannelHandler> channelHandlers = Lists.newArrayList();

	private final List<ChannelFutureListener> channelFutureListeners = Lists.newArrayList();

	private int channelThreads;

	private int boundToPort = -1;

	private EventLoopGroup channelEventLoopGroup;

	@Setter
	private boolean autoStartup = false;

	@Getter
	private ChannelOptions channelOptions = new ChannelOptions();

	@Getter
	private Channel channel;

	public NettyClient(String name, String host, int port) {
		this.name = name;
		this.host = host;
		this.port = port;
		this.channelThreads = Runtime.getRuntime().availableProcessors();
	}

	public ListenableFuture<Void> startup() {
		log.info("Starting Netty client '{}' with {} threads", name, channelThreads);

		final SettableFuture<Void> result = SettableFuture.create(); // 锁住返回结果
		final Bootstrap bootstrap = checkState().createBootstrap();
		final Channel channel = bootstrap.bind(host, port).syncUninterruptibly().channel();

		new Thread(
			() -> {
				final InetSocketAddress boundTo = (InetSocketAddress) channel.localAddress();
				final String hostAddress = boundTo.getAddress().getHostAddress();

				boundToPort = boundTo.getPort();
				log.info("Started Netty client '{}' @{}:{}", name, hostAddress, boundToPort);

				result.set(null);
				channel.closeFuture().syncUninterruptibly();
			},
			name)
			.start();

		return result;
	}

	public void shutdown() {
		log.info("Stopping Netty client '{}'", name);

		channelEventLoopGroup.shutdownGracefully();
		channelEventLoopGroup.terminationFuture().syncUninterruptibly();
	}

	public void addChannelHandler(final ChannelHandler channelHandler) {
		checkState().channelHandlers.add(channelHandler);
	}

	public void addAllChannelHandlers(final List<ChannelHandler> channelHandlers) {
		checkState().channelHandlers.addAll(channelHandlers);
	}

	public void addChannelFutureListener(final ChannelFutureListener channelFutureListener) {
		checkState().channelFutureListeners.add(channelFutureListener);
	}

	public void addAllChannelFutureListeners(
		final List<ChannelFutureListener> channelFutureListeners) {
		checkState().channelFutureListeners.addAll(channelFutureListeners);
	}

	public void setChannelThreads(final int channelThreads) {
		checkState().channelThreads =
			channelThreads > this.channelThreads ? this.channelThreads : channelThreads;
	}

	public boolean isInitialized() {
		return initialized.get();
	}

	private Bootstrap createBootstrap() {
		channelEventLoopGroup = new NioEventLoopGroup(channelThreads);

		final Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(channelEventLoopGroup);

		setOptions(bootstrap);
		initialized.set(true);

		return initBootstrap(bootstrap);
	}

	private Bootstrap initBootstrap(final Bootstrap bootstrap) {
		return bootstrap
			.channel(NioSocketChannel.class)
			.handler(
				new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(final SocketChannel socketChannel) {
						initChannelHandlers(socketChannel);
						initChannelFutureListeners(socketChannel);
					}
				})
			.validate();
	}

	private void initChannelHandlers(final SocketChannel channel) {
		if (!channelHandlers.isEmpty()) {
			final ChannelPipeline pipeline = channel.pipeline();
			for (final ChannelHandler handler : channelHandlers) {
				pipeline.addLast(handler);
			}
		}
	}

	private void initChannelFutureListeners(final SocketChannel channel) {
		if (!channelFutureListeners.isEmpty()) {
			for (final ChannelFutureListener listener : channelFutureListeners) {
				channel.closeFuture().addListener(listener);
			}
		}
	}

	private void setOptions(final Bootstrap bootstrap) {
		for (final Map.Entry<ChannelOption, Object> entry : channelOptions.get().entrySet()) {
			bootstrap.option(entry.getKey(), entry.getValue());
		}
	}

	private NettyClient checkState() {
		if (initialized.get()) {
			throw new IllegalStateException("Netty client already initialized");
		}
		return this;
	}
}
