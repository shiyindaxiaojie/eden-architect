package org.ylzl.eden.sample.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.ylzl.eden.spring.boot.data.jpa.id.JpaIdentifierGenerator;

import javax.persistence.*;
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
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Entity
@GenericGenerator(name = JpaIdentifierGenerator.NAME, strategy = JpaIdentifierGenerator.STRATEGY)
@Table(name = "sample_user")
public class User {

	private static final long serialVersionUID = 5816295498204890068L;

	@ApiModelProperty(value = "主键")
	@Id
	@GeneratedValue(generator = JpaIdentifierGenerator.NAME)
	private Long id;

	@ApiModelProperty(value = "账号", required = true)
	@Column(name = "login", length = 20, unique = true, nullable = false)
	private String login;

	@ApiModelProperty(value = "密码", required = true)
	@Column(name = "password_hash", length = 60, nullable = false)
	private String password;

	@ApiModelProperty(value = "邮箱")
	@Column(length = 254, unique = true)
	private String email;

	@ApiModelProperty(hidden = true)
	@Column(nullable = false)
	private Boolean activated = false;

	@ApiModelProperty(hidden = true)
	@Column(nullable = false)
	private Boolean locked = false;

	@ApiModelProperty(value = "语言")
	@Column(name = "lang_key", length = 6)
	private String langKey;

	@ApiModelProperty(hidden = true)
	@Column(name = "activation_key", length = 20, insertable = false)
	@JsonIgnore
	private String activationKey;

	@ApiModelProperty(hidden = true)
	@Column(name = "reset_key", length = 20, insertable = false)
	@JsonIgnore
	private String resetKey;

	@ApiModelProperty(hidden = true)
	@Column(name = "reset_date", insertable = false)
	@JsonIgnore
	private Date resetDate;

	@ApiModelProperty(value = "创建帐号", hidden = true)
	@CreatedBy
	@Column(name = "created_by", nullable = false, length = 20, updatable = false)
	private String createdBy;

	@ApiModelProperty(value = "创建时间", hidden = true)
	@CreatedDate
	@Column(name = "created_date", nullable = false)
	private Date createdDate;

	@ApiModelProperty(value = "最后修改帐号", hidden = true)
	@LastModifiedBy
	@Column(name = "last_modified_by", length = 20, insertable = false)
	private String lastModifiedBy;

	@ApiModelProperty(value = "最后修改时间", hidden = true)
	@LastModifiedDate
	@Column(name = "last_modified_date", insertable = false)
	private Date lastModifiedDate;
}
