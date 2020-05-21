/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ylzl.eden.spring.boot.integration.netty.server;

import com.google.common.base.Supplier;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Netty 服务端
 *
 * @author gyl
 * @since 1.0.0
 */
@Slf4j
public class NettyServer {

  private final AtomicBoolean initialized = new AtomicBoolean(false);

  private final String name;

  private final String host;

  private final Integer port;

  private int bossThreads;

  private int workerThreads;

  private int boundToPort = -1;

  /** 一组接收客户端请求的线程池 */
  private EventLoopGroup bossEventLoopGroup;

  /** 一组 IO 工作线程池 */
  private EventLoopGroup workerEventLoopGroup;

  private final Map<String, Supplier<ChannelHandler>> channelHandlers = Maps.newLinkedHashMap();

  private final List<Supplier<ChannelHandler>> activeChannelHandlers = Lists.newArrayList();

  private final List<Supplier<ChannelFutureListener>> closeFutureListeners = Lists.newArrayList();

  private final Map<String, Object> channelOptions = Maps.newHashMap();

  private final Map<String, Object> childChannelOptions = Maps.newHashMap();

  public NettyServer(String name, String host, Integer port) {
    this.name = name;
    this.host = host;
    this.port = port;
    this.bossThreads = Runtime.getRuntime().availableProcessors();
    this.workerThreads = Runtime.getRuntime().availableProcessors();
  }

  public ListenableFuture<Void> startup() {
    log.info(
        "Starting Netty server `{}` with {} boss threads and {} worker threads",
        name,
        bossThreads,
        workerThreads);

    if (!activeChannelHandlers.isEmpty()) {
      int i = 1;
      for (final Supplier<ChannelHandler> handler : activeChannelHandlers) {
        addChannelHandler("ActiveChannel" + i++, handler);
      }
    }

    final SettableFuture<Void> result = SettableFuture.create(); // 锁住返回结果
    final ServerBootstrap bootstrap = checkState().createServerBootstrap();
    final Channel channel = bootstrap.bind(host, port).syncUninterruptibly().channel();

    new Thread(
            () -> {
              final InetSocketAddress boundTo = (InetSocketAddress) channel.localAddress();
              final String hostName = boundTo.getAddress().getHostName();

              boundToPort = boundTo.getPort();
              log.info("Started Netty server `{}` @{}:{}", name, hostName, boundToPort);

              result.set(null);
              channel.closeFuture().syncUninterruptibly();
            },
            name)
        .start();

    return result;
  }

  public void shutdown() {
    log.info("Stopping Netty server `{}`", name);

    workerEventLoopGroup.shutdownGracefully();
    bossEventLoopGroup.shutdownGracefully();

    workerEventLoopGroup.terminationFuture().syncUninterruptibly();
    bossEventLoopGroup.terminationFuture().syncUninterruptibly();
  }

  public void addChannelHandler(final String name, final Supplier<ChannelHandler> channelHandler) {
    checkState().channelHandlers.put(name, channelHandler);
  }

  public void setBossThreads(final Integer bossThreads) {
    checkState().bossThreads = bossThreads > this.bossThreads ? this.bossThreads : bossThreads;
  }

  public void setWorkerThreads(final Integer workerThreads) {
    checkState().workerThreads =
        workerThreads > this.workerThreads ? this.workerThreads : workerThreads;
  }

  private ServerBootstrap createServerBootstrap() {
    bossEventLoopGroup = new NioEventLoopGroup(bossThreads);
    workerEventLoopGroup = new NioEventLoopGroup(workerThreads);

    final ServerBootstrap bootstrap = new ServerBootstrap();
    bootstrap.group(bossEventLoopGroup, workerEventLoopGroup);

    setOptions(bootstrap);
    initialized.set(true);

    return initServerBootstrap(bootstrap);
  }

  private ServerBootstrap initServerBootstrap(final ServerBootstrap bootstrap) {
    return bootstrap
        .channel(NioServerSocketChannel.class)
        .childHandler(
            new ChannelInitializer<SocketChannel>() {
              @Override
              protected void initChannel(final SocketChannel socketChannel) {
                initChildChannel(socketChannel);
              }
            })
        .validate();
  }

  private void initChildChannel(final SocketChannel channel) {
    final ChannelPipeline pipeline = channel.pipeline();
    for (final Map.Entry<String, Supplier<ChannelHandler>> entry : channelHandlers.entrySet()) {
      final ChannelHandler channelHandler = entry.getValue().get();
      final String key = entry.getKey();
      pipeline.addLast(key, channelHandler);
    }

    if (!closeFutureListeners.isEmpty()) {
      for (final Supplier<ChannelFutureListener> supplier : closeFutureListeners) {
        final ChannelFutureListener listener = supplier.get();
        channel.closeFuture().addListener(listener);
      }
    }
  }

  private void setOptions(final ServerBootstrap bootstrap) {
    for (final Map.Entry<String, Object> entry : channelOptions.entrySet()) {
      ChannelOption channelOption = ChannelOption.valueOf(entry.getKey());
      if (channelOption != null) {
        bootstrap.option(channelOption, entry.getValue());
      }
    }
    for (final Map.Entry<String, Object> entry : childChannelOptions.entrySet()) {
      ChannelOption channelOption = ChannelOption.valueOf(entry.getKey());
      if (channelOption != null) {
        bootstrap.childOption(channelOption, entry.getValue());
      }
    }
  }

  private NettyServer checkState() {
    if (initialized.get()) {
      throw new IllegalStateException("Netty Server already initialized");
    }
    return this;
  }
}
