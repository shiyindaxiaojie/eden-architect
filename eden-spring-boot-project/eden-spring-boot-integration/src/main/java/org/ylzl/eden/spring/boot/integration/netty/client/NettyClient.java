package org.ylzl.eden.spring.boot.integration.netty.client;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.ylzl.eden.spring.boot.integration.netty.channel.ChannelOptions;
import org.ylzl.eden.spring.boot.integration.netty.server.NettyServer;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Netty 客户端
 *
 * @author gyl
 * @since 2.0.0
 */
@Slf4j
public class NettyClient {

	private final AtomicBoolean initialized = new AtomicBoolean(false);

	private final String name;

	private final String host;

	private final Integer port;

	@Setter
	private Boolean autoStartup = false;

	private int channelThreads;

	private int boundToPort = -1;

	private EventLoopGroup channelEventLoopGroup;

	/** 一组 IO 工作线程池 */
	private final List<ChannelHandler> channelHandlers = Lists.newArrayList();

	private final List<ChannelFutureListener> channelFutureListeners = Lists.newArrayList();

	private ChannelOptions channelOptions = new ChannelOptions();

	public NettyClient(String name, String host, int port) {
		this.name = name;
		this.host = host;
		this.port = port;
		this.channelThreads = Runtime.getRuntime().availableProcessors();
	}

	public ListenableFuture<Void> startup() {
		log.info(
			"Starting Netty client `{}` with {} threads",
			name,
			channelThreads);

		final SettableFuture<Void> result = SettableFuture.create(); // 锁住返回结果
		final Bootstrap bootstrap = checkState().createBootstrap();
		final Channel channel = bootstrap.bind(host, port).syncUninterruptibly().channel();

		new Thread(
			() -> {
				final InetSocketAddress boundTo = (InetSocketAddress) channel.localAddress();
				final String hostName = boundTo.getAddress().getHostName();

				boundToPort = boundTo.getPort();
				log.info("Started Netty client `{}` @{}:{}", name, hostName, boundToPort);

				result.set(null);
				channel.closeFuture().syncUninterruptibly();
			},
			name)
			.start();

		return result;
	}

	public void shutdown() {
		log.info("Stopping Netty client `{}`", name);

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
		checkState().channelThreads = channelThreads > this.channelThreads ? this.channelThreads : channelThreads;
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
						initChildChannel(socketChannel);
					}
				})
			.validate();
	}

	private void initChildChannel(final SocketChannel channel) {
		if (!channelHandlers.isEmpty()) {
			final ChannelPipeline pipeline = channel.pipeline();
			for (final ChannelHandler handler : channelHandlers) {
				pipeline.addLast(handler);
			}
		}

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
