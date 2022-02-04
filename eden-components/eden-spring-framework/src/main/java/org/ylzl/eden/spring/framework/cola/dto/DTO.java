package org.ylzl.eden.spring.framework.cola.dto;

import lombok.*;

import java.io.Serializable;

/**
 * 数据传输对象
 *
 * @author Frank Zhang
 * @author gyl
 * @since 2.4.x
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public abstract class DTO implements Serializable {

	private static final long serialVersionUID = 1L;
}
