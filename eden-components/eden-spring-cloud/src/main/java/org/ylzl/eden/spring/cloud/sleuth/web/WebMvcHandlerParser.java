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

package org.ylzl.eden.spring.cloud.sleuth.web;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.cloud.sleuth.SpanCustomizer;
import org.springframework.cloud.sleuth.instrument.web.mvc.HandlerParser;
import org.ylzl.eden.commons.lang.ObjectUtils;
import org.ylzl.eden.commons.lang.StringUtils;
import org.ylzl.eden.commons.lang.Strings;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

/**
 * WebMvc 链路跟踪解析扩展
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@EqualsAndHashCode(callSuper = false)
@ToString
@Data
public class WebMvcHandlerParser extends HandlerParser {

	public static final String CONTROLLER_REQUEST_HEADER_PREFIX =
		"mvc.controller.request.header.";

	public static final String CONTROLLER_REQUEST_PARAMETER_PREFIX =
		"mvc.controller.request.parameter.";

	private static final String ALL_PATTERN = "*";

	private String ignoreHeaders;

	private String ignoreParameters;

	@Override
	protected void preHandle(HttpServletRequest request, Object handler, SpanCustomizer customizer) {
		super.preHandle(request, handler, customizer);

		if (!isAllIgnored(ignoreHeaders)) {
			List<String> ignoreList = StringUtils.isNotEmpty(ignoreParameters) ?
				Arrays.asList(ignoreHeaders.split(Strings.COMMA)) : null;
			handleHeader(ignoreList, request, customizer);
		}

		if (!isAllIgnored(ignoreParameters)) {
			List<String> ignoreList = StringUtils.isNotEmpty(ignoreParameters) ?
				Arrays.asList(ignoreParameters.split(Strings.COMMA)) : null;
			handleParameter(ignoreList, request, customizer);
		}
	}

	private void handleHeader(List<String> ignoreList, HttpServletRequest request, SpanCustomizer customizer) {
		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String key = headerNames.nextElement();
			if (ignored(ignoreList, key)) {
				continue;
			}

			String value = request.getHeader(key);
			customizer.tag(CONTROLLER_REQUEST_HEADER_PREFIX + key, value);
		}
	}

	private void handleParameter(List<String> ignoreList, HttpServletRequest request, SpanCustomizer customizer) {
		Map<String, String[]> parameterMap = request.getParameterMap();
		for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
			String key = entry.getKey();
			if (ignored(ignoreList, key)) {
				continue;
			}

			String[] value = entry.getValue();
			if (ObjectUtils.isEmpty(value)) {
				customizer.tag(CONTROLLER_REQUEST_PARAMETER_PREFIX + key, Strings.EMPTY);
			} else {
				StringBuilder values = new StringBuilder();
				for (String val : value) {
					values.append(val).append(Strings.DOT);
				}
				if (values.indexOf(Strings.DOT) >= 0) {
					values.delete(values.length() - 1, values.length());
				}
				customizer.tag(CONTROLLER_REQUEST_PARAMETER_PREFIX + key, values.toString());
			}
		}
	}

	private boolean isAllIgnored(String pattern) {
		return ALL_PATTERN.equals(pattern);
	}

	private boolean ignored(List<String> ignoreList, String key) {
		if (CollectionUtils.isEmpty(ignoreList)) {
			return false;
		}

		return ignoreList.contains(key);
	}
}
