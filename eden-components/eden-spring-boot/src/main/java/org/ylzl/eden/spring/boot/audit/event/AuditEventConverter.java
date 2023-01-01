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

package org.ylzl.eden.spring.boot.audit.event;

import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.ylzl.eden.spring.boot.audit.repository.PersistenceAuditEvent;

import java.util.*;

/**
 * 审计事件数据转换器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class AuditEventConverter {

	public static final String REMOTE_ADDRESS = "remoteAddress";

	public static final String SESSION_ID = "sessionId";

	public List<AuditEvent> convertToAuditEvent(Iterable<PersistenceAuditEvent> persistentAuditEvents) {
		if (persistentAuditEvents == null) {
			return Collections.emptyList();
		}

		List<AuditEvent> auditEvents = new ArrayList<>();
		for (PersistenceAuditEvent persistenceAuditEvent : persistentAuditEvents) {
			auditEvents.add(convertToAuditEvent(persistenceAuditEvent));
		}
		return auditEvents;
	}

	public AuditEvent convertToAuditEvent(PersistenceAuditEvent persistenceAuditEvent) {
		if (persistenceAuditEvent == null) {
			return null;
		}
		return new AuditEvent(
			persistenceAuditEvent.getEventDate(),
			persistenceAuditEvent.getPrincipal(),
			persistenceAuditEvent.getEventType(),
			convertDataToObjects(persistenceAuditEvent.getData()));
	}

	public Map<String, Object> convertDataToObjects(Map<String, String> data) {
		Map<String, Object> results = new HashMap<>();
		if (data != null) {
			results.putAll(data);
		}
		return results;
	}

	public Map<String, String> convertDataToStrings(Map<String, Object> data) {
		Map<String, String> results = new HashMap<>();
		if (data != null) {
			for (Map.Entry<String, Object> entry : data.entrySet()) {
				if (entry.getValue() instanceof WebAuthenticationDetails) {
					WebAuthenticationDetails authenticationDetails =
						(WebAuthenticationDetails) entry.getValue();
					results.put(REMOTE_ADDRESS, authenticationDetails.getRemoteAddress());
					results.put(SESSION_ID, authenticationDetails.getSessionId());
				} else {
					results.put(entry.getKey(), Objects.toString(entry.getValue()));
				}
			}
		}
		return results;
	}
}
