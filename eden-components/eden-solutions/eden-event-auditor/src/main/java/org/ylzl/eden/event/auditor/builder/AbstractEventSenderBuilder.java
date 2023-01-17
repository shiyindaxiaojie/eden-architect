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

package org.ylzl.eden.event.auditor.builder;

import org.ylzl.eden.event.auditor.config.EventAuditorConfig;

/**
 * 事件发送构建器抽象
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public abstract class AbstractEventSenderBuilder implements EventSenderBuilder {

	private EventAuditorConfig eventAuditorConfig = new EventAuditorConfig();

	/**
	 * 设置事件审计配置
	 *
	 * @param eventAuditorConfig 事件审计配置
	 */
	@Override
	public EventSenderBuilder setEventAuditorConfig(EventAuditorConfig eventAuditorConfig) {
		this.eventAuditorConfig = eventAuditorConfig;
		return this;
	}

	/**
	 * 获取事件审计配置
	 *
	 * @return 事件审计配置
	 */
	protected EventAuditorConfig getEventAuditorConfig() {
		return eventAuditorConfig;
	}
}
