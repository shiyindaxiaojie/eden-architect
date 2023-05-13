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

package org.ylzl.eden.data.filter.sensitive.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonInputMessage;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;
import org.ylzl.eden.commons.lang.Strings;
import org.ylzl.eden.data.filter.Sensitive;
import org.ylzl.eden.data.filter.SensitiveFilter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

/**
 * 敏感词请求消息体拦截声明
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@RequiredArgsConstructor
@Slf4j
@RestControllerAdvice
public class SensitiveRequestBodyAdvice extends RequestBodyAdviceAdapter {

	private final SensitiveFilter sensitiveFilter;

	@Override
	public boolean supports(MethodParameter methodParameter, Type targetType,
							Class<? extends HttpMessageConverter<?>> converterType) {
		return targetType.getClass().isAnnotationPresent(Sensitive.class);
	}

	@Override
	public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter,
										   Type targetType, Class<? extends HttpMessageConverter<?>> converterType)
		throws IOException {
		String body = StreamUtils.copyToString(inputMessage.getBody(), StandardCharsets.UTF_8);
		String sensitiveBody = doFilter(body, targetType.getClass(), sensitiveFilter);
		ByteArrayInputStream inputStream = new ByteArrayInputStream(sensitiveBody.getBytes(StandardCharsets.UTF_8));
		return new MappingJacksonInputMessage(inputStream, inputMessage.getHeaders());
	}

	/**
	 * 执行过滤
	 *
	 * @param text            文本内容
	 * @param clazz           目标类型
	 * @param sensitiveFilter 敏感词过滤器
	 * @return 过滤后的内容
	 */
	private static String doFilter(String text, Class<?> clazz, SensitiveFilter sensitiveFilter) {
		Sensitive sensitive = clazz.getAnnotation(Sensitive.class);
		String replacement = null;
		switch (sensitive.strategy()) {
			case NONE:
				return text;
			case DELETE:
				replacement = Strings.EMPTY;
				break;
			case REPLACE:
				replacement = sensitive.replacement();
				break;
		}
		return sensitiveFilter.replaceSensitiveWords(text, replacement);
	}
}
