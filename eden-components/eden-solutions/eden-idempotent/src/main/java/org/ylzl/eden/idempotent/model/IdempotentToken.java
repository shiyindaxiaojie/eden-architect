package org.ylzl.eden.idempotent.model;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * 幂等令牌
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Data
public class IdempotentToken {

	private String value;
}
