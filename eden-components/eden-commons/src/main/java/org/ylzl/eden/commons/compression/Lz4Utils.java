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
import net.jpountz.lz4.LZ4FrameInputStream;
import net.jpountz.lz4.LZ4FrameOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Lz4 压缩工具集
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@UtilityClass
public class Lz4Utils {

	public static byte[] compress(byte[] data, int level) throws IOException {
		try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(data.length);
			 LZ4FrameOutputStream outputStream = new LZ4FrameOutputStream(byteArrayOutputStream)) {
			outputStream.write(data);
			outputStream.flush();
			outputStream.close();
			return byteArrayOutputStream.toByteArray();
		}
	}

	public static byte[] decompress(byte[] data) throws IOException {
		byte[] b = new byte[data.length];
		try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
			 LZ4FrameInputStream lz4InputStream = new LZ4FrameInputStream(byteArrayInputStream);
			 ByteArrayOutputStream resultOutputStream = new ByteArrayOutputStream(data.length)) {
			while (true) {
				int len = lz4InputStream.read(b, 0, b.length);
				if (len <= 0) {
					break;
				}
				resultOutputStream.write(b, 0, len);
			}
			resultOutputStream.flush();
			resultOutputStream.close();
			return resultOutputStream.toByteArray();
		}
	}
}
