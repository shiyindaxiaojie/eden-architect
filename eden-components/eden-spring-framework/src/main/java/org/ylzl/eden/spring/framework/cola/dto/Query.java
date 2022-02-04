package org.ylzl.eden.spring.framework.cola.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * 查询（CQRS）
 *
 * @author Frank Zhang
 * @author gyl
 * @since 2.4.x
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public abstract class Query extends DTO {

	private static final long serialVersionUID = 1L;
}
