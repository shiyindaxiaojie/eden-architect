package org.ylzl.eden.spring.security.core.token;

import lombok.*;

/**
 * 刷新令牌
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class RefreshToken {

	private String value;
}
