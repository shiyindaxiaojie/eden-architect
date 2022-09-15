/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ylzl.eden.spring.data.audit.repository;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.actuate.audit.AuditEventRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.ylzl.eden.spring.data.audit.event.AuditEventConverter;
import org.ylzl.eden.spring.data.audit.event.PersistentAuditEvent;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 认证的审计事件数据仓库
 *
 * <p>变更日志：Spring Boot 升级 1.X 到 2.X
 *
 * <ul>
 *   <li>{@code find(String principal, Date after, String type)} 变更为 {@code find(String principal,
 *       Instant after, String type)}
 *   <li>{@code find(Date after)} 被移除
 *   <li>{@code find(String principal, Date after)} 被移除
 * </ul>
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
@Slf4j
@Data
@NoRepositoryBean
public abstract class PersistenceAuditEventRepositoryAdapter<
	T extends PersistentAuditEvent, ID extends Serializable>
	implements AuditEventRepository {

	private final PersistenceAuditEventRepository<PersistentAuditEvent, ID>
		persistenceAuditEventRepository;

	private final AuditEventConverter auditEventConverter;

	private int eventDataColumnMaxLength = 255;

	public abstract PersistentAuditEvent createPersistentAuditEvent(AuditEvent event);

	@Override
	public void add(AuditEvent event) {
		PersistentAuditEvent persistentAuditEvent = createPersistentAuditEvent(event);
		if (persistentAuditEvent == null) {
			return;
		}
		persistentAuditEvent.setPrincipal(event.getPrincipal());
		persistentAuditEvent.setEventType(event.getType());
		persistentAuditEvent.setEventDate(event.getTimestamp());
		Map<String, String> eventData = auditEventConverter.convertDataToStrings(event.getData());
		persistentAuditEvent.setData(truncate(eventData));
		persistenceAuditEventRepository.save(persistentAuditEvent);
	}

	@Override
	public List<AuditEvent> find(String principal, Instant after, String type) {
		Iterable<PersistentAuditEvent> persistentAuditEvents =
			persistenceAuditEventRepository.findByPrincipalAndEventDateAfterAndEventType(
				principal, after, type);
		return auditEventConverter.convertToAuditEvent(persistentAuditEvents);
	}

	private Map<String, String> truncate(Map<String, String> data) {
		Map<String, String> results = new HashMap<>();
		if (data != null) {
			for (Map.Entry<String, String> entry : data.entrySet()) {
				String value = entry.getValue();
				if (value != null) {
					int length = value.length();
					if (length > eventDataColumnMaxLength) {
						value = value.substring(0, eventDataColumnMaxLength);
					}
				}
				results.put(entry.getKey(), value);
			}
		}
		return results;
	}
}
