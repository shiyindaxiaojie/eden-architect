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

package org.ylzl.eden.common.excel.exporter;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.ylzl.eden.common.excel.ExcelExporter;
import org.ylzl.eden.common.excel.builder.ExcelWriterBuilder;
import org.ylzl.eden.commons.env.Charsets;
import org.ylzl.eden.commons.id.NanoIdUtils;
import org.ylzl.eden.commons.lang.MessageFormatUtils;
import org.ylzl.eden.spring.framework.error.util.AssertUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * Excel 导出处理
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@RequiredArgsConstructor
@Slf4j
public class ExcelExportHandler implements HandlerMethodReturnValueHandler {

	private static final String DEFAULT_CONTENT_TYPE = "application/vnd.ms-excel";

	private static final String DEFAULT_CONTENT_DISPOSITION = "attachment;filename*={}''{}";

	private final ExcelWriterBuilder excelWriterBuilder;

	@Override
	public boolean supportsReturnType(MethodParameter parameter) {
		return parameter.getMethodAnnotation(ExcelExporter.class) != null;
	}

	@SneakyThrows(Exception.class)
	@Override
	public void handleReturnValue(Object returnValue, MethodParameter parameter,
								  ModelAndViewContainer mavContainer, NativeWebRequest webRequest) {
		ExcelExporter excelExporter = parameter.getMethodAnnotation(ExcelExporter.class);
		AssertUtils.notNull(excelExporter, "@ExcelImporter is null");

		HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
		mavContainer.setRequestHandled(true);

	}

	@SneakyThrows({
		UnsupportedEncodingException.class,
		IOException.class
	})
	public void export(HttpServletResponse response, List<Object> data, ExcelExporter excelExporter) {
		String name = excelExporter.name();
		if (name == null) {
			name = NanoIdUtils.randomNanoId();
		}

		String fileName = URLEncoder.encode(name, Charsets.UTF_8_NAME) + excelExporter.format().getValue();
		String contentType = MediaTypeFactory.getMediaType(fileName)
			.map(MediaType::toString)
			.orElse(DEFAULT_CONTENT_TYPE);
		response.setContentType(contentType);
		response.setCharacterEncoding(Charsets.UTF_8_NAME);
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
			MessageFormatUtils.format(DEFAULT_CONTENT_DISPOSITION, Charsets.UTF_8_NAME, fileName));
		// TODO

		excelWriterBuilder.build(excelExporter.format(), excelExporter.inMemory())
			.write(response.getOutputStream(), data, null);
	}
}
