package org.ylzl.eden.spring.framework.extension;

import lombok.experimental.UtilityClass;

import java.util.regex.Pattern;

/**
 * 扩展点常量定义
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@UtilityClass
public class ExtensionConstants {

	public static final Pattern COMMA_SPLIT_PATTERN = Pattern.compile("\\s*[,]+\\s*");

	public static final String REMOVE_VALUE_PREFIX = "-";

	public static final String DEFAULT_KEY = "default";

	public static final String REMOVE_DEFAULT_KEY = REMOVE_VALUE_PREFIX + DEFAULT_KEY;
}
