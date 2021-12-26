package org.ylzl.eden.spring.data.audit.repository;

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
 * @author gyl
 * @since 2.0.0
 */
@SuppressWarnings("unchecked")
@Slf4j
@NoRepositoryBean
public abstract class PersistenceAuditEventRepositoryAdapter<
        T extends PersistentAuditEvent, ID extends Serializable>
    implements AuditEventRepository {

  private final PersistenceAuditEventRepository<PersistentAuditEvent, ID>
      persistenceAuditEventRepository;

  private final AuditEventConverter auditEventConverter;

  private int eventDataColumnMaxLength = 255;

  public PersistenceAuditEventRepositoryAdapter(
      PersistenceAuditEventRepository<PersistentAuditEvent, ID> persistenceAuditEventRepository,
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
