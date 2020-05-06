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

package org.ylzl.eden.spring.boot.integration.netty.bootstrap;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.ylzl.eden.spring.boot.integration.netty.channel.ChannelInitalizerAdapter;

import java.net.InetSocketAddress;

/**
 * TODO
 *
 * @author gyl
 * @since 0.0.1
 */
@Slf4j
public class NettyServer {

  /** 客户端连接处理 */
  private final EventLoopGroup bossEventLoopGroup = new NioEventLoopGroup();

  /** 客户端 I/O 事件处理 */
  private final EventLoopGroup workerEventLoopGroup = new NioEventLoopGroup();

  /** 网络通道 */
  private Channel channel;

  public ChannelFuture channel(InetSocketAddress inetSocketAddress) throws Exception {
    ChannelFuture channelFuture = null;
    try {
      ServerBootstrap bootstrap = new ServerBootstrap();
      bootstrap
          .group(bossEventLoopGroup, workerEventLoopGroup)
          .channel(NioServerSocketChannel.class)
          .option(ChannelOption.SO_BACKLOG, 1024) // 请求占满时临时存放的队列长度
          .childOption(ChannelOption.SO_KEEPALIVE, true) // 开启长连接
          .childHandler(new ChannelInitalizerAdapter());
      channelFuture = bootstrap.bind(inetSocketAddress).syncUninterruptibly();
      channel = channelFuture.channel();
    } finally {
      if (channel != null) {
        channel.closeFuture().syncUninterruptibly();
      }
    }
    return channelFuture;
  }

  @Synchronized
  public void shutdown() throws Exception {
    if (channel != null && channel.isOpen()) {
      channel.close();
      workerEventLoopGroup.shutdownGracefully();
      bossEventLoopGroup.shutdownGracefully();
    }
  }
}
