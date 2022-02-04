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

package org.ylzl.eden.spring.integration.netty.rpc.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.ylzl.eden.spring.integration.netty.rpc.serializer.Serializer;

/**
 * 写入数据时将消息对象编码为字节
 *
 * @author gyl
 * @since 2.0.0
 */
public class RpcWriteEncoder extends MessageToByteEncoder {

	private final Class<?> clazz;

	private final Serializer serializer;

	public RpcWriteEncoder(Class<?> clazz, Serializer serializer) {
		this.clazz = clazz;
		this.serializer = serializer;
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
		byte[] bytes = serializer.serialize(msg);
		out.writeInt(bytes.length);
		out.writeBytes(bytes);
	}
}
