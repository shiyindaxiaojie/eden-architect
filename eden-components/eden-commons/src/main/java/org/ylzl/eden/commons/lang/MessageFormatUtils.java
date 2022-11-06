package org.ylzl.eden.commons.lang;

import lombok.experimental.UtilityClass;
import org.ylzl.eden.commons.lang.format.MessageFormatter;

/**
 * 消息格式化工具
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@UtilityClass
public class MessageFormatUtils {

	/**
	 * 格式化消息内容
	 *
	 * @param message
	 * @param placeholders
	 * @return
	 */
	public static String format(String message, Object... placeholders) {
		return MessageFormatter.arrayFormat(message, placeholders).getMessage();
	}
}
