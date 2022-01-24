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

package org.ylzl.eden.spring.data.audit.event;

import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import java.util.*;

/**
 * 审计事件数据转换器
 *
 * @author gyl
 * @since 2.4.x
 */
public class AuditEventConverter {

	public static final String REMOTE_ADDRESS = "remoteAddress";

	public static final String SESSION_ID = "sessionId";

	public List<AuditEvent> convertToAuditEvent(
		Iterable<PersistentAuditEvent> persistentAuditEvents) {
		if (persistentAuditEvents == null) {
			return Collections.emptyList();
		}
		List<AuditEvent> auditEvents = new ArrayList<>();
		for (PersistentAuditEvent persistentAuditEvent : persistentAuditEvents) {
			auditEvents.add(convertToAuditEvent(persistentAuditEvent));
		}
		return auditEvents;
	}

	public AuditEvent convertToAuditEvent(PersistentAuditEvent persistentAuditEvent) {
		if (persistentAuditEvent == null) {
			return null;
		}
		return new AuditEvent(
			persistentAuditEvent.getEventDate(),
			persistentAuditEvent.getPrincipal(),
			persistentAuditEvent.getEventType(),
			convertDataToObjects(persistentAuditEvent.getData()));
	}

	public Map<String, Object> convertDataToObjects(Map<String, String> data) {
		Map<String, Object> results = new HashMap<>();
		if (data != null) {
			for (Map.Entry<String, String> entry : data.entrySet()) {
				results.put(entry.getKey(), entry.getValue());
			}
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
