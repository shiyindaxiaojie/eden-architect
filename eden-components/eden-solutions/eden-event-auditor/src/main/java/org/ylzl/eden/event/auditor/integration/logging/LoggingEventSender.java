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

package org.ylzl.eden.event.auditor.integration.logging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.event.Level;
import org.ylzl.eden.commons.collections.CollectionUtils;
import org.ylzl.eden.event.auditor.EventSender;
import org.ylzl.eden.event.auditor.model.AuditingEvent;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 基于 Slf4j 发送审计事件
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@RequiredArgsConstructor
@Slf4j
public class LoggingEventSender implements EventSender {

	public static final String AUDITING_EVENT = "Auditing event: {}";

	private final Level level;

	/**
	 * 发送审计事件列表
	 *
	 * @param events 审计事件列表
	 */
	@Override
	public void send(List<AuditingEvent> events) {
		if (CollectionUtils.isEmpty(events)) {
			return;
		}

		List<String> contents = events.stream()
			.map(AuditingEvent::getContent).collect(Collectors.toList());
		switch (level) {
			case DEBUG:
				log.debug(AUDITING_EVENT, contents);
				break;
			case INFO:
				log.info(AUDITING_EVENT, contents);
				break;
			case WARN:
				log.warn(AUDITING_EVENT, contents);
				break;
		}
	}
}
