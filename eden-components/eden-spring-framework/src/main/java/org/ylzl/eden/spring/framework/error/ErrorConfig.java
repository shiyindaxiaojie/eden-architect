package org.ylzl.eden.spring.framework.error;

import org.jetbrains.annotations.PropertyKey;
import org.slf4j.helpers.MessageFormatter;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * 错误码配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class ErrorConfig {

	public static final String BASE_NAME = "error.message";

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BASE_NAME,
		Locale.SIMPLIFIED_CHINESE);

	public static String getErrMessage(@PropertyKey(resourceBundle = BASE_NAME) String errCode,
									   Object... params) {
		if (RESOURCE_BUNDLE.containsKey(errCode)) {
			String value = RESOURCE_BUNDLE.getString(errCode);
			return MessageFormatter.arrayFormat(value, params).getMessage();
		}
		return MessageFormatter.arrayFormat(errCode, params).getMessage();
	}
}
