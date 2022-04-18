package org.ylzl.eden.spring.security.jwt.token;

import lombok.*;

/**
 * 令牌
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class JwtToken {

	/**
	 * 令牌
	 */
	private String value;
}
