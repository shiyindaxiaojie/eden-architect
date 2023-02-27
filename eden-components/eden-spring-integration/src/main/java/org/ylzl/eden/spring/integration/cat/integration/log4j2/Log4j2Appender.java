package org.ylzl.eden.spring.integration.cat.integration.log4j2;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Message;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.*;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.appender.AppenderLoggingException;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.impl.ThrowableProxy;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.util.Booleans;
import org.ylzl.eden.spring.integration.cat.config.CatState;

import java.io.Serializable;

/**
 * Log4j2 附加器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Plugin(name = Log4j2Appender.NAME, category = Core.CATEGORY_NAME, elementType = Appender.ELEMENT_TYPE, printObject = true)
public class Log4j2Appender extends AbstractAppender {

	public static final String NAME = "CAT";

	public static final String TYPE = "Log4j2";

	private final Level level;

	public Log4j2Appender(String name, Filter filter, Layout<? extends Serializable> layout,
						  boolean ignoreExceptions, Property[] properties, Level level) {
		super(name, filter, layout, ignoreExceptions, properties);
		this.level = level;
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
			case INFO:
			case WARN:
				Cat.logEvent(TYPE, event.getLevel().name(), Message.SUCCESS,
					event.getMessage().getFormattedMessage());
			case ERROR:
				ThrowableProxy proxy = event.getThrownProxy();
				if (proxy != null) {
					Throwable exception = proxy.getThrowable();
					if (event.getMessage() != null) {
						Cat.logError(event.getMessage().getFormattedMessage(), exception);
					} else {
						Cat.logError(exception);
					}
				}
		}
	}

	@PluginFactory
	public static Log4j2Appender build(
		@PluginAttribute("name") String name,
		@PluginElement("Filter") Filter filter,
		@PluginElement("Layout") Layout<? extends Serializable> layout,
		@PluginAttribute("ignoreExceptions") String ignore,
		@PluginAttribute("level") Level level) {

		if (name == null) {
			return null;
		}
		if (layout == null) {
			layout = PatternLayout.createDefaultLayout();
		}
		if (level == null) {
			level = Level.ERROR;
		}
		boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
		return new Log4j2Appender(name, filter, layout, ignoreExceptions, null, level);
	}
}
