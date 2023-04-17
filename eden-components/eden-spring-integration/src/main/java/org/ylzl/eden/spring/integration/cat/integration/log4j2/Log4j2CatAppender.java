package org.ylzl.eden.spring.integration.cat.integration.log4j2;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Message;
import lombok.SneakyThrows;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.*;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.appender.AppenderLoggingException;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.JsonLayout;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.util.Booleans;
import org.ylzl.eden.commons.env.Charsets;
import org.ylzl.eden.spring.integration.cat.CatConstants;
import org.ylzl.eden.spring.integration.cat.config.CatState;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

/**
 * Log4j2 附加器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Plugin(name = Log4j2CatAppender.NAME, category = Core.CATEGORY_NAME, elementType = Appender.ELEMENT_TYPE, printObject = true)
public class Log4j2CatAppender extends AbstractAppender {

	public static final String NAME = "CAT";

	private static final int DEFAULT_MAX_LENGTH = 500;

	private final Level level;

	private final Integer maxLength;

	public Log4j2CatAppender(String name, Filter filter, Layout<? extends Serializable> layout,
							 boolean ignoreExceptions, Property[] properties, Level level, Integer maxLength) {
		super(name, filter, layout, ignoreExceptions, properties);
		this.level = level;
		this.maxLength = maxLength != null && maxLength > 0? maxLength : DEFAULT_MAX_LENGTH;
	}

	@Override
	public void append(LogEvent event) {
		if (!CatState.isInitialized()) {
			return;
		}
		try {
			if (event.getLevel().isMoreSpecificThan(level)) {
				logCat(event, event.getLevel());
			}
		} catch (Exception ex) {
			if (!ignoreExceptions()) {
				throw new AppenderLoggingException(ex);
			}
		}
	}

	private void logCat(LogEvent event, Level level) {
		switch (level.getStandardLevel()) {
			case DEBUG:
			case INFO:
			case WARN:
				tryAppend(event);
			default:
				// 直接跳过，避免和业务重复输出
				break;
		}
	}

	@SneakyThrows(UnsupportedEncodingException.class)
	private void tryAppend(final LogEvent event) {
		Layout<? extends Serializable> layout = getLayout();
		byte[] data;
		if (layout instanceof JsonLayout) {
			final byte[] header = layout.getHeader();
			final byte[] body = layout.toByteArray(event);
			data = new byte[header.length + body.length];
			System.arraycopy(header, 0, data, 0, header.length);
			System.arraycopy(body, 0, data, header.length, body.length);
		} else {
			data = layout.toByteArray(event);
		}
		String message = data.length > maxLength?
			new String(data, 0, maxLength, Charsets.UTF_8_NAME) :
			new String(data, Charsets.UTF_8_NAME);
		Cat.logEvent(CatConstants.TYPE_LOG_LOG4J2, event.getLevel().name(), Message.SUCCESS, message);
	}

	@PluginFactory
	public static Log4j2CatAppender build(
		@PluginAttribute("name") String name,
		@PluginElement("Filter") Filter filter,
		@PluginElement("Layout") Layout<? extends Serializable> layout,
		@PluginAttribute("ignoreExceptions") String ignore,
		@PluginAttribute("level") Level level,
		@PluginAttribute("maxLength") Integer maxLength) {

		if (name == null) {
			return null;
		}
		if (layout == null) {
			layout = PatternLayout.createDefaultLayout();
		}
		if (level == null) {
			level = Level.ERROR;
		}
		if (maxLength == null) {
			maxLength = DEFAULT_MAX_LENGTH;
		}
		boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
		return new Log4j2CatAppender(name, filter, layout, ignoreExceptions, null, level, maxLength);
	}
}
