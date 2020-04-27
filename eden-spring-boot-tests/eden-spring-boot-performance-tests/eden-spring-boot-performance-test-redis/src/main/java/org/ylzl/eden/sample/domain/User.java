package org.ylzl.eden.sample.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

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
@RedisHash("users")
public class User {

	private static final long serialVersionUID = 5816295498204890068L;

	@Id
	private Long id;

	@Indexed
	private String login;

	private String password;

	@Indexed
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
