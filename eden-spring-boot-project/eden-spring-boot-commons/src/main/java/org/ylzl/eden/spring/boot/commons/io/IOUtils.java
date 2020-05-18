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

package org.ylzl.eden.spring.boot.commons.io;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * IO 工具集
 *
 * @author gyl
 * @since 0.0.1
 */
@UtilityClass
public class IOUtils extends org.apache.commons.io.IOUtils {

  public static void closeQuietly(@NonNull Closeable... closeables) {
    for (final Closeable closeable : closeables) {
      closeQuietly(closeable);
    }
  }

  public static void transferFrom(@NonNull FileChannel inChannel, @NonNull FileChannel outChannel)
      throws IOException {
    outChannel.transferFrom(inChannel, 0L, inChannel.size());
  }

  public static void transferTo(@NonNull FileChannel inChannel, @NonNull FileChannel outChannel)
      throws IOException {
    inChannel.transferTo(0L, inChannel.size(), outChannel);
  }

  public static void allocate(
      @NonNull FileChannel inChannel, @NonNull OutputStream out, boolean isDirect)
      throws IOException {
    int bufferSize = 4096;
    byte[] data = new byte[bufferSize];
    int capacity = bufferSize * 10;
    ByteBuffer bytebuffer =
        isDirect ? ByteBuffer.allocateDirect(capacity) : ByteBuffer.allocate(capacity);
    int read;
    int len;
    try {
      while ((read = inChannel.read(bytebuffer)) != -1) {
        if (read == 0) {
          continue;
        }
        bytebuffer.position(0);
        bytebuffer.limit(read);
        while (bytebuffer.hasRemaining()) {
          len = Math.min(bytebuffer.remaining(), bufferSize);
          bytebuffer.get(data, 0, len);
          out.write(data);
        }
        bytebuffer.clear();
      }
    } finally {
      bytebuffer.clear();
    }
  }

  @Deprecated
  public static void map(@NonNull FileChannel inChannel, @NonNull FileChannel outChannel)
      throws IOException {
    long size = inChannel.size();
    long pos = 0L;
    MappedByteBuffer mbb = inChannel.map(FileChannel.MapMode.READ_ONLY, pos, size);
    outChannel.write(mbb);
  }

  @Deprecated
  public static void write(@NonNull InputStream is, @NonNull OutputStream os) throws IOException {
    byte[] buffer = new byte[1024];
    int len;
    while ((len = is.read(buffer)) > 0) {
      os.write(buffer, 0, len);
    }
  }

  public static void seek(
      @NonNull RandomAccessFile raf, @NonNull OutputStream out, long startByte, long endByte)
      throws IOException {
    long transmitted = 0;
    int bufferSize = 4096;
    byte[] data = new byte[bufferSize];
    int len = 0;
    try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(out)) {
      raf.seek(startByte);
      while ((transmitted + len) <= endByte // 防止读取的 len 小于 bufferSize， transmitted 加上 len 时跳过不足的部分
          && (len = raf.read(data)) != -1) {
        bufferedOutputStream.write(data, 0, len);
        transmitted += len;
      }
      if (transmitted < endByte) { // 处理不足 bufferSize 的部分
        len = raf.read(data, 0, (int) (endByte - transmitted));
        bufferedOutputStream.write(data, 0, len);
      }
      bufferedOutputStream.flush();
    }
  }

  public static long seek(
      @NonNull RandomAccessFile inRaf, @NonNull RandomAccessFile outRaf, long begin, long end)
      throws IOException {
    byte[] data = new byte[4096];
    int n = 0;
    inRaf.seek(begin);
    while (inRaf.getFilePointer() <= end && (n = inRaf.read(data)) != -1) {
      outRaf.write(data, 0, n);
    }
    return inRaf.getFilePointer();
  }

  public static void seek(@NonNull RandomAccessFile inRaf, @NonNull RandomAccessFile outRaf)
      throws IOException {
    byte[] data = new byte[4096];
    int n = 0;
    while ((n = inRaf.read(data)) != -1) {
      outRaf.write(data, 0, n);
    }
  }
}
