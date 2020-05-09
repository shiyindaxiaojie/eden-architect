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

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.RandomAccessFile;

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

  public static void slice(File file, int chunks) throws IOException {
    try (RandomAccessFile raf = new RandomAccessFile(file, "r"); ) {
      long length = raf.length();
      long sliceSize = length / chunks;
      long offSet = 0L;
      for (int i = 0; i < chunks - 1; i++) {
        long begin = offSet;
        long end = (i + 1) * sliceSize;
        offSet = write(file, i, begin, end);
      }
      if (length - offSet > 0) {
        write(file, chunks - 1, offSet, length);
      }
    }
  }

  private static long write(File file, int index, long begin, long end) throws IOException {
    String sourcePath = file.getAbsolutePath();
    File tempFile =
        new File(sourcePath.substring(0, sourcePath.indexOf(".")) + "_" + index + ".tmp");
    try (RandomAccessFile inRaf = new RandomAccessFile(file, "r");
        RandomAccessFile outRaf = new RandomAccessFile(tempFile, "rw"); ) {
      byte[] b = new byte[4096];
      int n = 0;
      inRaf.seek(begin);
      while (inRaf.getFilePointer() <= end && (n = inRaf.read(b)) != -1) {
        outRaf.write(b, 0, n);
      }
      return inRaf.getFilePointer();
    }
  }

  public static void merge(String mergeFileName, File... tempFiles) throws IOException {
    String sourcePath = tempFiles[0].getAbsolutePath();
    String mergeFilePath = sourcePath.substring(0, sourcePath.indexOf(".")) + mergeFileName;
    try (RandomAccessFile outRaf = new RandomAccessFile(mergeFilePath, "rw"); ) {
      for (File tempFile : tempFiles) {
        try (RandomAccessFile reader = new RandomAccessFile(tempFile, "r"); ) {
          byte[] b = new byte[4096];
          int n = 0;
          while ((n = reader.read(b)) != -1) {
            outRaf.write(b, 0, n);
          }
        }
      }
    }
  }

  public static void merge(File file, File tempPath) throws IOException {
    final String mergeFileName = file.getName();
    final String mergeFileNamePrefix = mergeFileName.substring(0, mergeFileName.indexOf("."));
    File[] tempFiles =
        tempPath.listFiles(
            new FilenameFilter() {

              @Override
              public boolean accept(File dir, String name) {
                return name.startsWith(mergeFileNamePrefix) && name.endsWith(".tmp");
              }
            });
    merge(mergeFileName, tempFiles);
  }

  public static void main(String[] args) throws IOException {
    File file = new File("C:\\Users\\sion1\\Documents\\Workspaces\\MySQL技术内幕InnoDB存储引擎.pdf");
    File tempPath = new File("C:\\Users\\sion1\\Documents\\Workspaces\\");
    FileUtils.merge(file, tempPath);
  }
}
