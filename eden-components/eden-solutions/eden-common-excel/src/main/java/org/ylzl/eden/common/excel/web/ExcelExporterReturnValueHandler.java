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

package org.ylzl.eden.common.excel.web;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.ylzl.eden.common.excel.ExcelExporter;
import org.ylzl.eden.spring.framework.error.util.AssertUtils;

import javax.servlet.http.HttpServletResponse;

/**
 * ExcelExporter 返回值解析
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Slf4j
public class ExcelExporterReturnValueHandler implements HandlerMethodReturnValueHandler {

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
}
