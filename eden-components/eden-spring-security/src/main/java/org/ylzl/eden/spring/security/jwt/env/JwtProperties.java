package org.ylzl.eden.spring.security.jwt.env;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * JWT 配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Data
@ConfigurationProperties(prefix = "security.jwt")
public class JwtProperties {

	private boolean enabled;

	private String base64Secret;

	private String secret;

	private long tokenValidityInSeconds = 1800;

	private long tokenValidityInSecondsForRememberMe = 2592000;

	private String header = "Authorization";

	private List<String> anonymousUrls;

	private List<String> permitAllUrls;

	private List<String> authenticatedUrls;
}
