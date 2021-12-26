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

package org.ylzl.eden.spring.integration.netty.bootstrap;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.ylzl.eden.spring.integration.netty.channel.ChannelOptions;

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
public class NettyServer implements InitializingBean, DisposableBean {

  private final AtomicBoolean initialized = new AtomicBoolean(false);

  private final String name;

  private final String host;

  private final Integer port;
  private final List<ChannelHandler> channelHandlers = Lists.newArrayList();
  private final List<ChannelFutureListener> channelFutureListeners = Lists.newArrayList();
  @Getter private final ChannelOptions channelOptions = new ChannelOptions();
  @Getter private final ChannelOptions childChannelOptions = new ChannelOptions();
  private int bossThreads;
  private int workerThreads;
  private int boundToPort = -1;
  private EventLoopGroup bossEventLoopGroup;
  private EventLoopGroup workerEventLoopGroup;
  @Setter private Boolean autoStartup = false;

  public NettyServer(String name, String host, int port) {
    this.name = name;
    this.host = host;
    this.port = port;
    this.bossThreads = Runtime.getRuntime().availableProcessors();
    this.workerThreads = Runtime.getRuntime().availableProcessors();
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    if (autoStartup) {
      checkState().startup();
    }
  }

  @Override
  public void destroy() throws Exception {
    shutdown();
  }

  public ListenableFuture<Void> startup() {
    log.info(
        "Starting Netty server `{}` with {} boss threads and {} worker threads",
        name,
        bossThreads,
        workerThreads);

    final SettableFuture<Void> result = SettableFuture.create(); // 锁住返回结果
    final ServerBootstrap bootstrap = checkState().createServerBootstrap();
    final Channel channel = bootstrap.bind(host, port).syncUninterruptibly().channel();

    new Thread(
            () -> {
              final InetSocketAddress boundTo = (InetSocketAddress) channel.localAddress();
              final String hostAddress = boundTo.getAddress().getHostAddress();

              boundToPort = boundTo.getPort();
              log.info("Started Netty server `{}` @{}:{}", name, hostAddress, boundToPort);

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

  public void setBossThreads(final int bossThreads) {
    checkState().bossThreads = bossThreads > this.bossThreads ? this.bossThreads : bossThreads;
  }

  public void setWorkerThreads(final int workerThreads) {
    checkState().workerThreads =
        workerThreads > this.workerThreads ? this.workerThreads : workerThreads;
  }

  public boolean isInitialized() {
    return initialized.get();
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

  private void setOptions(final ServerBootstrap bootstrap) {
    for (final Map.Entry<ChannelOption, Object> entry : channelOptions.get().entrySet()) {
      bootstrap.option(entry.getKey(), entry.getValue());
    }

    for (final Map.Entry<ChannelOption, Object> entry : childChannelOptions.get().entrySet()) {
      bootstrap.childOption(entry.getKey(), entry.getValue());
    }
  }

  private NettyServer checkState() {
    if (initialized.get()) {
      throw new IllegalStateException("Netty Server already initialized");
    }
    return this;
  }
}
