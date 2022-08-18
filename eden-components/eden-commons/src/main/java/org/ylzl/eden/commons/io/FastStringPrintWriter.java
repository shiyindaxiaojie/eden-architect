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
