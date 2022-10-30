package org.ylzl.eden.spring.framework.extension.common;

import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/**
 * 自定义资源路径
 *
 * @see java.net.URL
 * @see java.net.URI
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
public class URL implements Serializable {

	private final Map<String, String> parameters;

	public String getParameter(String key) {
		return parameters.get(key);
	}

	public Map<String, String> getParameters() {
		return parameters;
	}
}
