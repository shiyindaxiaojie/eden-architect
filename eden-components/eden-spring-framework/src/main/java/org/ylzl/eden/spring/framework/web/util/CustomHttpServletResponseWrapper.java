package org.ylzl.eden.spring.framework.web.util;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * HttpServletResponse 包装器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class CustomHttpServletResponseWrapper extends HttpServletResponseWrapper {

	private final ByteArrayOutputStream outputStream;

	private final PrintWriter writer;

	public CustomHttpServletResponseWrapper(HttpServletResponse response) {
		super(response);
		outputStream = new ByteArrayOutputStream();
		writer = new PrintWriter(outputStream, true);
	}

	@Override
	public PrintWriter getWriter() {
		return writer;
	}

	public String getContent() {
		return new String(outputStream.toByteArray(), StandardCharsets.UTF_8);
	}
}
