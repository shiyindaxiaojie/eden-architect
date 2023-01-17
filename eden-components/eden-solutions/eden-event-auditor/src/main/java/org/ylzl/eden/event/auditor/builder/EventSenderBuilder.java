package org.ylzl.eden.event.auditor.builder;

import org.ylzl.eden.event.auditor.EventSender;
import org.ylzl.eden.event.auditor.config.EventAuditorConfig;
import org.ylzl.eden.extension.SPI;

/**
 * 事件发送构建器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@SPI("logging")
public interface EventSenderBuilder {

	/**
	 * 设置事件审计配置
	 *
	 * @param eventAuditorConfig 事件审计配置
	 */
	EventSenderBuilder setEventAuditorConfig(EventAuditorConfig eventAuditorConfig);

	/**
	 * 构建事件审计实例
	 *
	 * @return 事件审计实例
	 */
	EventSender build();
}
