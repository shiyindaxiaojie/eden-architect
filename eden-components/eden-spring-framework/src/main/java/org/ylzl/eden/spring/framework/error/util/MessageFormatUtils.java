package org.ylzl.eden.spring.framework.error.util;

import lombok.experimental.UtilityClass;
import org.slf4j.helpers.MessageFormatter;

/**
 * 消息格式化工具
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@UtilityClass
public class MessageFormatUtils {

	public static String format(String message, Object... placeholders) {
		return MessageFormatter.arrayFormat(message, placeholders).getMessage();
	}
}
