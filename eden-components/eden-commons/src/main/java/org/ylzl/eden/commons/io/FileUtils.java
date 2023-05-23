/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ylzl.eden.commons.io;

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
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @see org.apache.commons.io.FileUtils
 * @since 2.4.13
 */
@UtilityClass
public class FileUtils extends org.apache.commons.io.FileUtils {

	public static boolean mkdirs(@NonNull String dirPath) {
		return mkdirs(new File(dirPath));
	}

	public static boolean mkdirs(@NonNull File file) {
		if (file.exists()) {
			return true;
		}

		return file.mkdirs();
	}

	public static boolean checkAndMkdirs(@NonNull File file) {
		File parentFile = file.getParentFile();
		if (!parentFile.exists()) {
			return parentFile.mkdirs();
		}
		return true;
	}

	public static void delete(File file) throws IOException {
		java.nio.file.Files.delete(Paths.get(file.getAbsolutePath()));
	}

	public static void deleteIfExists(File file) throws IOException {
		java.nio.file.Files.deleteIfExists(Paths.get(file.getAbsolutePath()));
	}

	public static void transferTo(@NonNull FileInputStream in, @NonNull String destPath)
		throws IOException {
		try (FileChannel inChannel = in.getChannel();
			 FileChannel outChannel =
				 FileChannel.open(
					 Paths.get(destPath),
					 EnumSet.of(
						 StandardOpenOption.CREATE_NEW,
						 StandardOpenOption.TRUNCATE_EXISTING,
						 StandardOpenOption.WRITE))) {
			IOUtils.transferTo(inChannel, outChannel);
		}
	}

	public static void transferTo(@NonNull String srcPath, @NonNull FileOutputStream out)
		throws IOException {
		try (FileChannel inChannel =
				 FileChannel.open(Paths.get(srcPath), EnumSet.of(StandardOpenOption.READ));
			 FileChannel outChannel = out.getChannel()) {
			IOUtils.transferTo(inChannel, outChannel);
		}
	}

	public static void transferTo(@NonNull String srcPath, @NonNull String destPath)
		throws IOException {
		try (FileChannel inChannel =
				 FileChannel.open(Paths.get(srcPath), EnumSet.of(StandardOpenOption.READ));
			 FileChannel outChannel =
				 FileChannel.open(
					 Paths.get(destPath),
					 EnumSet.of(
						 StandardOpenOption.CREATE_NEW,
						 StandardOpenOption.TRUNCATE_EXISTING,
						 StandardOpenOption.WRITE))) {
			IOUtils.transferTo(inChannel, outChannel);
		}
	}

	public static void allocate(@NonNull String srcPath, @NonNull OutputStream out)
		throws IOException {
		try (FileChannel inChannel =
				 FileChannel.open(Paths.get(srcPath), EnumSet.of(StandardOpenOption.READ))) {
			IOUtils.allocate(inChannel, out, false);
		}
	}

	public static void allocateDirect(@NonNull String srcPath, @NonNull OutputStream out)
		throws IOException {
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

	public static void seek(
		@NonNull File file, @NonNull OutputStream out, long startByte, long endByte)
		throws IOException {
		try (RandomAccessFile randomAccessFile =
				 new RandomAccessFile(file, Files.RAF_MODE_READ)) {
			IOUtils.seek(randomAccessFile, out, startByte, endByte);
		}
	}

	public static void seek(@NonNull File file, @NonNull OutputStream out, long startByte)
		throws IOException {
		seek(file, out, startByte, file.length());
	}

	public static void slice(@NonNull File file, int chunks, @NonNull String suffix)
		throws IOException {
		String filePath = file.getAbsolutePath();
		try (FileInputStream fis = new FileInputStream(filePath);
			 FileChannel inChannel = fis.getChannel();) {
			long length = inChannel.size();
			long sliceSize = length / chunks;
			slice(file, sliceSize, suffix, inChannel, length, chunks);
		}
	}

	private static void slice(
		@NonNull File file,
		long sliceSize,
		@NonNull String suffix,
		FileChannel inChannel,
		long length,
		int chunks)
		throws IOException {

		for (int i = 0; i < chunks; i++) {
			long pos = i * sliceSize;
			long count = i == chunks - 1 ? length - pos : sliceSize;
			try (FileOutputStream fos = getTempFileOutputStream(file, suffix, i);
				 FileChannel outChannel = fos.getChannel();) {
				inChannel.transferTo(pos, count, outChannel);
			}
		}
	}

	private static FileOutputStream getTempFileOutputStream(
		@NonNull File file, @NonNull String suffix, int index) throws FileNotFoundException {
		String sourcePath = file.getAbsolutePath();
		return new FileOutputStream(
			sourcePath.substring(0, sourcePath.indexOf(Files.FILE_SEPARATOR)) + index + suffix);
	}

	public static void merge(
		@NonNull File mergeFile,
		@NonNull File tempDir,
		@NonNull final String matchPrefix,
		@NonNull final String matchSuffix)
		throws IOException {
		merge(mergeFile, tempDir, matchPrefix, matchSuffix, true);
	}

	public static void merge(
		@NonNull File mergeFile,
		@NonNull File tempDir,
		@NonNull final String matchPrefix,
		@NonNull final String matchSuffix,
		boolean cleanTemp)
		throws IOException {
		if (!tempDir.isDirectory()) {
			throw new IOException("the tempDir must be given directory");
		}
		File[] tempFiles =
			tempDir.listFiles(
				(dir, name) -> name.startsWith(matchPrefix) && name.endsWith(matchSuffix));
		merge(mergeFile, cleanTemp, tempFiles);
	}

	public static void merge(@NonNull File mergeFile, boolean cleanTemp, @NonNull File... tempFiles)
		throws IOException {
		if (mergeFile.exists()) {
			delete(mergeFile);
		}
		try (FileChannel outChannel =
				 FileChannel.open(
					 Paths.get(mergeFile.getAbsolutePath()),
					 EnumSet.of(
						 StandardOpenOption.CREATE_NEW,
						 StandardOpenOption.TRUNCATE_EXISTING,
						 StandardOpenOption.WRITE))) {
			for (File tempFile : tempFiles) {
				try (FileChannel inChannel =
						 FileChannel.open(
							 Paths.get(tempFile.getAbsolutePath()), EnumSet.of(StandardOpenOption.READ));) {
					IOUtils.transferTo(inChannel, outChannel);
				}
			}
		} finally {
			if (cleanTemp) {
				for (File tempFile : tempFiles) {
					delete(tempFile);
				}
			}
		}
	}

	public static byte[] toBytes(@NonNull File file) throws IOException {
		int bufferSize = 1024;
		try (FileInputStream fis = new FileInputStream(file);
			 ByteArrayOutputStream bos = new ByteArrayOutputStream(bufferSize);) {
			byte[] b = new byte[bufferSize];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			return bos.toByteArray();
		}
	}

	public static File toFile(@NonNull byte[] bytes, @NonNull File file) throws IOException {
		checkAndMkdirs(file);
		try (FileOutputStream fos = new FileOutputStream(file);
			 BufferedOutputStream bos = new BufferedOutputStream(fos);) {
			bos.write(bytes);
		}
		return file;
	}
}
