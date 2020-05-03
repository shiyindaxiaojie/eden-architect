package org.ylzl.eden.sample.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户领域
 *
 * @author gyl
 * @since 0.0.1
 */
@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode
@NoArgsConstructor
@ToString
@RedisHash(value = "users", timeToLive = 30L)
public class User implements Serializable {

  private static final long serialVersionUID = 6719807913047955885L;

  @Id private Long id;

  @Indexed // 生成 users:login:login.value 和
  private String login;

  private String password;

  @Indexed // 生成 users:email:email.value
  private String email;

  private Boolean activated = false;

  private Boolean locked = false;

  private String langKey;

  private String activationKey;

  private String resetKey;

  private Date resetDate;

  private String createdBy;

  private Date createdDate;

  private String lastModifiedBy;

  private Date lastModifiedDate;
}
