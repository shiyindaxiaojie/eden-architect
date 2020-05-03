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
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;

/**
 * 输入输出工具集
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

  public static long transferFrom(FileChannel inChannel, FileChannel outChannel)
      throws IOException {
    long start = System.currentTimeMillis();
    long size = inChannel.size();
    long pos = 0L;
    for (long count; pos < size; pos += outChannel.transferFrom(inChannel, pos, count)) {
      count = Math.min(size - pos, 31457280L); // 30M
    }
    long end = System.currentTimeMillis();
    return end - start;
  }

  public static long transferFrom(FileInputStream in, String destPath) throws IOException {
    try (FileChannel inChannel = in.getChannel();
        FileChannel outChannel =
            FileChannel.open(
                Paths.get(destPath),
                EnumSet.of(
                    StandardOpenOption.CREATE_NEW,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.WRITE))) {
      return transferFrom(inChannel, outChannel);
    }
  }

  public static long transferFrom(String srcPath, FileOutputStream out) throws IOException {
    try (FileChannel inChannel =
            FileChannel.open(Paths.get(srcPath), EnumSet.of(StandardOpenOption.READ));
        FileChannel outChannel = out.getChannel()) {
      return transferFrom(inChannel, outChannel);
    }
  }

  public static long transferFrom(String srcPath, String destPath) throws IOException {
    try (FileChannel inChannel =
            FileChannel.open(Paths.get(srcPath), EnumSet.of(StandardOpenOption.READ));
        FileChannel outChannel =
            FileChannel.open(
                Paths.get(destPath),
                EnumSet.of(
                    StandardOpenOption.CREATE_NEW,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.WRITE))) {
      return transferFrom(inChannel, outChannel);
    }
  }

  public static long allocate(String srcPath, OutputStream out, boolean isDirect)
      throws IOException {
    long start = System.currentTimeMillis();
    int bufferSize = 4096;
    byte[] datas = new byte[bufferSize];
    int capacity = bufferSize * 10;
    ByteBuffer bytebuffer =
        isDirect ? ByteBuffer.allocateDirect(capacity) : ByteBuffer.allocate(capacity);
    int read;
    int len;
    try (FileChannel inChannel =
        FileChannel.open(Paths.get(srcPath), EnumSet.of(StandardOpenOption.READ))) {
      while ((read = inChannel.read(bytebuffer)) != -1) {
        if (read == 0) {
          continue;
        }
        bytebuffer.position(0);
        bytebuffer.limit(read);
        while (bytebuffer.hasRemaining()) {
          len = Math.min(bytebuffer.remaining(), bufferSize);
          bytebuffer.get(datas, 0, len);
          out.write(datas);
        }
        bytebuffer.clear();
      }
    } finally {
      bytebuffer.clear();
    }
    long end = System.currentTimeMillis();
    return end - start;
  }

  public static long allocate(String srcPath, OutputStream out) throws IOException {
    return allocate(srcPath, out, false);
  }

  public static long allocateDirect(String srcPath, OutputStream out) throws IOException {
    return allocate(srcPath, out, true);
  }

  public static long seek(String srcPath, OutputStream out, long startByte, long endByte)
      throws IOException {
    File srcFile = new File(srcPath);
    long transmitted = 0;
    int bufferSize = 4096;
    byte[] datas = new byte[bufferSize];
    int len = 0;
    long start = System.currentTimeMillis();
    try (RandomAccessFile randomAccessFile = new RandomAccessFile(srcFile, "r");
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(out)) {
      randomAccessFile.seek(startByte);
      while ((transmitted + len) <= endByte // 防止读取的 len 小于 bufferSize， transmitted 加上 len 时跳过不足的部分
          && (len = randomAccessFile.read(datas)) != -1) {
        bufferedOutputStream.write(datas, 0, len);
        transmitted += len;
      }
      if (transmitted < endByte) { // 处理不足 bufferSize 的部分
        len = randomAccessFile.read(datas, 0, (int) (endByte - transmitted));
        bufferedOutputStream.write(datas, 0, len);
      }
      bufferedOutputStream.flush();
    }
    long end = System.currentTimeMillis();
    return end - start;
  }

  public static long seek(String srcPath, OutputStream out, long startByte) throws IOException {
    File srcFile = new File(srcPath);
    return seek(srcPath, out, startByte, srcFile.length());
  }

  @Deprecated
  public static long map(FileChannel inChannel, FileChannel outChannel) throws IOException {
    long size = inChannel.size();
    long pos = 0L;
    long start = System.currentTimeMillis();
    MappedByteBuffer mbb = inChannel.map(FileChannel.MapMode.READ_ONLY, pos, size);
    outChannel.write(mbb);
    long end = System.currentTimeMillis();
    return end - start;
  }

  @Deprecated
  public static long write(String srcPath, String destPath) throws IOException {
    long start = System.currentTimeMillis();
    byte[] buffer = new byte[1024];
    int len;
    try (InputStream is = new FileInputStream(new File(srcPath));
        OutputStream os = new FileOutputStream(new File(destPath))) {
      while ((len = is.read(buffer)) > 0) {
        os.write(buffer, 0, len);
      }
    }
    long end = System.currentTimeMillis();
    return end - start;
  }
}
