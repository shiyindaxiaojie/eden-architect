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

package org.ylzl.eden.commons.compression;

import lombok.experimental.UtilityClass;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

/**
 * Zlib 压缩工具集
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@UtilityClass
public class ZlibUtils {

	public static byte[] compress(byte[] data, int level) throws IOException {
		Deflater defeater = new Deflater(level);
		try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(data.length);
			 DeflaterOutputStream deflaterOutputStream = new DeflaterOutputStream(byteArrayOutputStream, defeater)) {
			deflaterOutputStream.write(data);
			deflaterOutputStream.finish();
			deflaterOutputStream.close();
			return byteArrayOutputStream.toByteArray();
		} finally {
			defeater.end();
		}
	}

	public static byte[] decompress(byte[] data) throws IOException {
		byte[] b = new byte[data.length];
		try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
			 InflaterInputStream inflaterInputStream = new InflaterInputStream(byteArrayInputStream);
			 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(data.length)) {
			while (true) {
				int len = inflaterInputStream.read(b, 0, b.length);
				if (len <= 0) {
					break;
				}
				byteArrayOutputStream.write(b, 0, len);
			}
			byteArrayOutputStream.flush();
			return byteArrayOutputStream.toByteArray();
		}
	}
}
