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

import cn.hutool.core.io.FastStringWriter;

import java.io.PrintWriter;

/**
 * 更快的 PrintWriter
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class FastStringPrintWriter extends PrintWriter {
	private final FastStringWriter writer;

	public FastStringPrintWriter() {
		this(256);
	}

	public FastStringPrintWriter(int initialSize) {
		super(new FastStringWriter(initialSize));
		this.writer = (FastStringWriter) out;
	}

	@Override
	public void println(Object x) {
		writer.write(String.valueOf(x));
		writer.write("\n");
	}

	@Override
	public String toString() {
		return writer.toString();
	}
}
