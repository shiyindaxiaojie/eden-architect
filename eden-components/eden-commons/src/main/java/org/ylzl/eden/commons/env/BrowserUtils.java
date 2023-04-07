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
package org.ylzl.eden.commons.env;

import com.google.common.net.HttpHeaders;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.ylzl.eden.commons.env.browser.Browser;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 浏览器工具集
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@UtilityClass
public class BrowserUtils {

	public static final String UNKNOWN_BROWSER = "Unknown Browser";

	public static String getLanguage(@NonNull HttpServletRequest request) {
		return request.getLocale().getLanguage();
	}

	public static String resolveValue(@NonNull String value) throws UnsupportedEncodingException {
		return URLEncoder.encode(value, String.valueOf(Charsets.UTF_8));
	}

	public static String parseBrowser(@NonNull HttpServletRequest request) {
		Browser browser = Browser.parse(request);
		if (browser != null) {
			return Browser.parse(request).getName();
		}
		return UNKNOWN_BROWSER;
	}

	public static String parseBrowserWithVersion(@NonNull HttpServletRequest request) {
		String userAgent = request.getHeader(HttpHeaders.USER_AGENT);
		return parseBrowserWithVersion(userAgent);
	}

	public static String parseBrowserWithVersion(@NonNull String userAgent) {
		Browser browser = Browser.parse(userAgent);
		if (browser != null) {
			return browser.getName() + browser.getVersion(userAgent);
		}
		return UNKNOWN_BROWSER;
	}
}
