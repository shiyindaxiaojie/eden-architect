package org.ylzl.eden.spring.boot.integration.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Synchronized;

import java.net.InetSocketAddress;

/**
 * Netty 客户端
 *
 * @author gyl
 * @since 2.0.0
 */
public class NettyClient {

	/** 一组 IO 工作线程池 */
	private final EventLoopGroup channelEventLoopGroup = new NioEventLoopGroup();

	private Channel channel;

	public ChannelFuture startup(InetSocketAddress address, ChannelHandler handler) {
		Bootstrap bootstrap = new Bootstrap();
		bootstrap
			.group(channelEventLoopGroup)
			.channel(NioSocketChannel.class)
			.option(ChannelOption.SO_KEEPALIVE, true) // 开启长连接
			.handler(handler);

		ChannelFuture channelFuture = bootstrap.bind(address).syncUninterruptibly();
		channel = channelFuture.channel();
		return channelFuture;
	}

	@Synchronized
	public void shutdown() {
		if (channel != null && channel.isOpen()) {
			channel.close();
			channelEventLoopGroup.shutdownGracefully();
		}
	}
}
