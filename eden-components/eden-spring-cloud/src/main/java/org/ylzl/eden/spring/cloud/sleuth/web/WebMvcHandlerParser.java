package org.ylzl.eden.spring.cloud.sleuth.web;

import lombok.Data;
import org.springframework.cloud.sleuth.SpanCustomizer;
import org.springframework.cloud.sleuth.instrument.web.mvc.HandlerParser;
import org.ylzl.eden.commons.collections.CollectionUtils;
import org.ylzl.eden.commons.lang.ObjectUtils;
import org.ylzl.eden.commons.lang.StringConstants;
import org.ylzl.eden.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

/**
 * WebMvc 链路跟踪解析扩展
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Data
public class WebMvcHandlerParser extends HandlerParser {

	public static final String CONTROLLER_REQUEST_HEADER_PREFIX =
		"mvc.controller.request.header.";

	public static final String CONTROLLER_REQUEST_PARAMETER_PREFIX =
		"mvc.controller.request.parameter.";

	private static final String ALL_PATCH = "*";

	private String ignoreHeaders;

	private String ignoreParameters;

	@Override
	protected void preHandle(HttpServletRequest request, Object handler, SpanCustomizer customizer) {
		super.preHandle(request, handler, customizer);

		if (isAllIgnored(ignoreHeaders)) {
			List<String> ignoreList = StringUtils.isNotEmpty(ignoreParameters)?
				Arrays.asList(ignoreHeaders.split(StringConstants.COMMA)) : null;
			handleHeader(ignoreList, request, customizer);
		}

		if (isAllIgnored(ignoreParameters)) {
			List<String> ignoreList = StringUtils.isNotEmpty(ignoreParameters)?
				Arrays.asList(ignoreParameters.split(StringConstants.COMMA)) : null;
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
				customizer.tag(CONTROLLER_REQUEST_PARAMETER_PREFIX + key, StringConstants.EMPTY);
			} else {
				StringBuilder values = new StringBuilder();
				for (String val : value) {
					values.append(val).append(StringConstants.DOT);
				}
				if (values.indexOf(StringConstants.DOT) >= 0) {
					values.delete(values.length() - 1, values.length());
				}
				customizer.tag(CONTROLLER_REQUEST_PARAMETER_PREFIX + key, values.toString());
			}
		}
	}

	private boolean isAllIgnored(String ignoreKeys) {
		return ALL_PATCH.equals(ignoreKeys);
	}

	private boolean ignored(List<String> ignoreList, String key) {
		if (CollectionUtils.isEmpty(ignoreList)) {
			return false;
		}

		return ignoreList.contains(key);
	}
}
