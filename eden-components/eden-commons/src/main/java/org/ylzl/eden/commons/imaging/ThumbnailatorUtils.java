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
package org.ylzl.eden.commons.imaging;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.name.Rename;

import java.io.File;
import java.io.IOException;

/**
 * Thumbnailator 工具集
 *
 * @author gyl
 * @since 2.4.x
 */
@UtilityClass
public class ThumbnailatorUtils {

	public static void compress(
		@NonNull File srcFile, @NonNull File destFile, long limitSize, double scale)
		throws IOException {
		Thumbnails.of(srcFile).scale(scale).toFile(destFile);
		if (destFile.length() > limitSize) {
			compress(destFile, destFile, limitSize);
		}
	}

	public static void compress(@NonNull File srcFile, @NonNull File destFile, long limitSize)
		throws IOException {
		compress(srcFile, destFile, limitSize, 0.9);
	}

	public static void zoom(@NonNull File srcFile, @NonNull File destFile, int width, int height)
		throws IOException {
		Thumbnails.of(srcFile).size(width, height).keepAspectRatio(false).toFile(destFile);
	}

	public static void zoom(
		@NonNull String[] fileNameArr, @NonNull File destFile, int width, int height, Rename rename)
		throws IOException {
		Thumbnails.of(fileNameArr).size(width, height).keepAspectRatio(false).toFiles(destFile, rename);
	}

	public static void zoom(
		@NonNull String[] fileNameArr, @NonNull File destFile, int width, int height) {
		zoom(fileNameArr, destFile, width, height);
	}
}
