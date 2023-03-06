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

package org.ylzl.eden.common.excel.importer;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartRequest;
import org.ylzl.eden.common.excel.ExcelImporter;
import org.ylzl.eden.common.excel.builder.ExcelReaderBuilder;
import org.ylzl.eden.common.excel.reader.ExcelReadListener;
import org.ylzl.eden.spring.framework.error.util.AssertUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

/**
 * Excel 导入处理
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@RequiredArgsConstructor
@Slf4j
public class ExcelImportHandler implements HandlerMethodArgumentResolver {

	private static final String OBJECT_NAME = "excel";

	private final ExcelReaderBuilder excelReaderBuilder;

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(ExcelImporter.class);
	}

	@SneakyThrows(Exception.class)
	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
								  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
		Class<?> parameterType = parameter.getParameterType();
		AssertUtils.isAssignable(parameterType, List.class,
			"@ExcelImporter parameter '" + parameterType + "' is not assign from List");

		ExcelImporter excelImporter = parameter.getParameterAnnotation(ExcelImporter.class);
		AssertUtils.notNull(excelImporter, "@ExcelImporter is not null");

		Class<? extends ExcelReadListener<?>> eventListenerClass = excelImporter.readEventListener();
		ExcelReadListener<?> readListener = BeanUtils.instantiateClass(eventListenerClass);

		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
		InputStream inputStream = request instanceof MultipartRequest?
			Objects.requireNonNull(((MultipartRequest) request).getFile(excelImporter.fileName())).getInputStream():
			Objects.requireNonNull(request).getInputStream();
		Class<?> targetClass = ResolvableType.forMethodParameter(parameter).getGeneric(0).resolve();
		excelReaderBuilder.build(excelImporter.headRowNumber(), excelImporter.ignoreEmptyRow())
			.read(inputStream, targetClass, readListener);

		WebDataBinder dataBinder = binderFactory.createBinder(webRequest, readListener.getErrors(), OBJECT_NAME);
		ModelMap model = mavContainer.getModel();
		model.put(BindingResult.MODEL_KEY_PREFIX + OBJECT_NAME, dataBinder.getBindingResult());
		return readListener.getBatchData();
	}
}
