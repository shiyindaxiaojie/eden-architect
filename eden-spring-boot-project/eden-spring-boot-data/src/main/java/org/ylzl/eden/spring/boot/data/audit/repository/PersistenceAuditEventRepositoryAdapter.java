package org.ylzl.eden.spring.boot.data.audit.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.actuate.audit.AuditEventRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.ylzl.eden.spring.boot.data.audit.event.AuditEventConverter;
import org.ylzl.eden.spring.boot.data.audit.event.PersistentAuditEvent;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 认证的审计事件数据仓库
 *
 * @author gyl
 * @since 1.0.0
 */
@SuppressWarnings("unchecked")
@Slf4j
@NoRepositoryBean
public abstract class PersistenceAuditEventRepositoryAdapter implements AuditEventRepository {

  private final PersistenceAuditEventRepository persistenceAuditEventRepository;

  private final AuditEventConverter auditEventConverter;

  private int eventDataColumnMaxLength = 255;

  public PersistenceAuditEventRepositoryAdapter(
      PersistenceAuditEventRepository persistenceAuditEventRepository,
      AuditEventConverter auditEventConverter) {
    this.persistenceAuditEventRepository = persistenceAuditEventRepository;
    this.auditEventConverter = auditEventConverter;
  }

  public PersistenceAuditEventRepositoryAdapter(
      PersistenceAuditEventRepository persistenceAuditEventRepository,
      AuditEventConverter auditEventConverter,
      int eventDataColumnMaxLength) {
    this.persistenceAuditEventRepository = persistenceAuditEventRepository;
    this.auditEventConverter = auditEventConverter;
    this.eventDataColumnMaxLength = eventDataColumnMaxLength;
  }

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
  public List<AuditEvent> find(Date after) {
    Iterable<PersistentAuditEvent> persistentAuditEvents =
        persistenceAuditEventRepository.findByEventDateAfter(after);
    return auditEventConverter.convertToAuditEvent(persistentAuditEvents);
  }

  @Override
  public List<AuditEvent> find(String principal, Date after) {
    Iterable<PersistentAuditEvent> persistentAuditEvents =
        persistenceAuditEventRepository.findByPrincipalAndEventDateAfter(principal, after);
    return auditEventConverter.convertToAuditEvent(persistentAuditEvents);
  }

  @Override
  public List<AuditEvent> find(String principal, Date after, String type) {
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
