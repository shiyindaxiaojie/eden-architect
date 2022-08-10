package org.ylzl.eden.spring.cloud.sleuth.web;

import org.springframework.cloud.sleuth.SpanCustomizer;
import org.springframework.cloud.sleuth.instrument.web.mvc.HandlerParser;
import org.ylzl.eden.commons.lang.ObjectUtils;
import org.ylzl.eden.commons.lang.StringConstants;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Map;

/**
 * WebMvc 链路跟踪解析扩展
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class WebMvcHandlerParser extends HandlerParser {

	public static final String CONTROLLER_REQUEST_HEADER_PREFIX =
		"mvc.controller.request.header.";

	public static final String CONTROLLER_REQUEST_PARAMETER_PREFIX =
		"mvc.controller.request.parameter.";

	@Override
	protected void preHandle(HttpServletRequest request, Object handler, SpanCustomizer customizer) {
		super.preHandle(request, handler, customizer);
		handleHeader(request, customizer);
		handleParameter(request, customizer);
	}

	private void handleHeader(HttpServletRequest request, SpanCustomizer customizer) {
		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String key = headerNames.nextElement();
			String value = request.getHeader(key);
			customizer.tag(CONTROLLER_REQUEST_HEADER_PREFIX + key, value);
		}
	}

	private void handleParameter(HttpServletRequest request, SpanCustomizer customizer) {
		Map<String, String[]> parameterMap = request.getParameterMap();
		for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
			String key = entry.getKey();
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
}
