package org.ylzl.eden.extension.common;

import lombok.experimental.UtilityClass;

import java.util.regex.Pattern;

/**
 * 常量定义
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@UtilityClass
public class Constants {

	public static final Pattern COMMA_SPLIT_PATTERN = Pattern.compile("\\s*[,]+\\s*");
}
