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
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;

/**
 * 文件工具集
 *
 * @author gyl
 * @since 0.0.1
 */
@UtilityClass
public class FileUtils extends org.apache.commons.io.FileUtils {

  public static boolean createDirectory(@NonNull String dirPath) {
    File file = new File(dirPath);
    if (file.exists()) {
      return true;
    }
    return file.mkdirs();
  }

  public static void transferFrom(@NonNull FileInputStream in, @NonNull String destPath) throws IOException {
    try (FileChannel inChannel = in.getChannel();
        FileChannel outChannel =
            FileChannel.open(
                Paths.get(destPath),
                EnumSet.of(
                    StandardOpenOption.CREATE_NEW,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.WRITE))) {
      IOUtils.transferFrom(inChannel, outChannel);
    }
  }

  public static void transferFrom(@NonNull String srcPath, @NonNull FileOutputStream out) throws IOException {
    try (FileChannel inChannel =
            FileChannel.open(Paths.get(srcPath), EnumSet.of(StandardOpenOption.READ));
        FileChannel outChannel = out.getChannel()) {
      IOUtils.transferFrom(inChannel, outChannel);
    }
  }

  public static void transferFrom(@NonNull String srcPath, @NonNull String destPath) throws IOException {
    try (FileChannel inChannel =
            FileChannel.open(Paths.get(srcPath), EnumSet.of(StandardOpenOption.READ));
        FileChannel outChannel =
            FileChannel.open(
                Paths.get(destPath),
                EnumSet.of(
                    StandardOpenOption.CREATE_NEW,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.WRITE))) {
      IOUtils.transferFrom(inChannel, outChannel);
    }
  }

  public static void allocate(@NonNull String srcPath, @NonNull OutputStream out) throws IOException {
    try (FileChannel inChannel =
        FileChannel.open(Paths.get(srcPath), EnumSet.of(StandardOpenOption.READ))) {
      IOUtils.allocate(inChannel, out, false);
    }
  }

  public static void allocateDirect(@NonNull String srcPath, @NonNull OutputStream out) throws IOException {
    try (FileChannel inChannel =
        FileChannel.open(Paths.get(srcPath), EnumSet.of(StandardOpenOption.READ))) {
      IOUtils.allocate(inChannel, out, true);
    }
  }

  @Deprecated
  public static void write(@NonNull String srcFile, @NonNull String destFile) throws IOException {
    try (InputStream is = new FileInputStream(srcFile);
        OutputStream os = new FileOutputStream(destFile)) {
      IOUtils.write(is, os);
    }
  }

  @Deprecated
  public static void write(@NonNull File srcFile, @NonNull File destFile) throws IOException {
    try (InputStream is = new FileInputStream(srcFile);
        OutputStream os = new FileOutputStream(destFile)) {
      IOUtils.write(is, os);
    }
  }

  public static void seek(@NonNull File file, @NonNull OutputStream out, long startByte, long endByte)
      throws IOException {
    try (RandomAccessFile randomAccessFile =
        new RandomAccessFile(file, IOConstants.RAF_MODE_READ)) {
      IOUtils.seek(randomAccessFile, out, startByte, endByte);
    }
  }

  public static void seek(@NonNull File file, @NonNull OutputStream out, long startByte) throws IOException {
     seek(file, out, startByte, file.length());
  }

  public static void slice(@NonNull File file, @NonNull String suffix, @NonNull File... chunkFiles) throws IOException {
    try (RandomAccessFile inRaf = new RandomAccessFile(file, IOConstants.RAF_MODE_READ)) {
      int chunks = chunkFiles.length;
      long length = inRaf.length();
      long sliceSize = length / chunks;
      long offSet = 0L;
      for (int i = 0; i < chunks - 1; i++) {
        long begin = offSet;
        long end = (i + 1) * sliceSize;
        offSet = IOUtils.slice(inRaf, getTempRandomAccessFile(file, suffix, i), begin, end);
      }
      if (length - offSet > 0) {
        IOUtils.slice(inRaf, getTempRandomAccessFile(file, suffix, chunks - 1), offSet, length);
      }
    }
  }

  private static RandomAccessFile getTempRandomAccessFile(@NonNull File file, @NonNull String suffix, int index)
      throws FileNotFoundException {
    String sourcePath = file.getAbsolutePath();
    File tempFile =
        new File(
            sourcePath.substring(0, sourcePath.indexOf(FileConstants.FILE_SEPARATOR))
                + index
                + suffix);
    return new RandomAccessFile(tempFile, IOConstants.RAF_MODE_READ_WRTIE);
  }

  public static void merge(
      @NonNull File mergeFile, @NonNull File tempDir, @NonNull final String matchPrefix,
	  @NonNull final String matchSuffix)
      throws IOException {
    if (!tempDir.isDirectory()) {
      throw new IOException("the tempDir must be given directory");
    }
    final String mergeFileNamePrefix =
        mergeFile.getName().substring(0, mergeFile.getName().indexOf(FileConstants.FILE_SEPARATOR));
    File[] tempFiles =
        tempDir.listFiles(
            new FilenameFilter() {

              @Override
              public boolean accept(File dir, String name) {
                return name.startsWith(matchPrefix) && name.endsWith(matchSuffix);
              }
            });
    merge(mergeFile, tempFiles);
  }

  public static void merge(@NonNull File mergeFile, @NonNull File... tempFiles) throws IOException {
    try (RandomAccessFile mergeRaf =
        new RandomAccessFile(mergeFile, IOConstants.RAF_MODE_READ_WRTIE); ) {
      for (File tempFile : tempFiles) {
        try (RandomAccessFile chunkRaf =
            new RandomAccessFile(tempFile, IOConstants.RAF_MODE_READ); ) {
          byte[] b = new byte[4096];
          int n = 0;
          while ((n = chunkRaf.read(b)) != -1) {
            mergeRaf.write(b, 0, n);
          }
        }
      }
    }
  }

  public static void merge(RandomAccessFile mergeRaf, RandomAccessFile chunkRaf) {}

  public static void main(String[] args) throws IOException {
    File mergeFile = new File("C:\\Users\\sion1\\Downloads\\test\\MySQL技术内幕InnoDB.pdf");
    String tempDir = "C:\\Users\\sion1\\Downloads\\test\\";
    FileUtils.merge(mergeFile, new File(tempDir), "MySQL技术内幕InnoDB", ".tmp");
  }
}
