package org.ylzl.eden.spring.framework.cola.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 命令（CQRS）
 *
 * @author Frank Zhang
 * @author gyl
 * @since 2.4.x
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public abstract class Command extends DTO {

	private static final long serialVersionUID = 1L;
}
