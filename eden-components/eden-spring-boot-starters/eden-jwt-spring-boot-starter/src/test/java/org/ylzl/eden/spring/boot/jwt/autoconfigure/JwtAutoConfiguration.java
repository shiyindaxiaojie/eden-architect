package org.ylzl.eden.spring.boot.jwt.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.ylzl.eden.spring.security.jwt.config.JwtConfig;
import org.ylzl.eden.spring.security.jwt.env.JwtProperties;
import org.ylzl.eden.spring.security.jwt.token.JwtTokenProvider;
import org.ylzl.eden.spring.security.jwt.token.JwtTokenService;

/**
 * JWT 自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@ConditionalOnExpression(JwtAutoConfiguration.SECURITY_JWT_ENABLED)
@EnableConfigurationProperties(JwtProperties.class)
@Slf4j
@Configuration
public class JwtAutoConfiguration {

	public static final String SECURITY_JWT_ENABLED = "${security.jwt.enabled:false}";

	private final JwtProperties jwtProperties;

	public JwtAutoConfiguration(JwtProperties jwtProperties) {
		this.jwtProperties = jwtProperties;
	}

	@ConditionalOnMissingBean
	@Bean
	public JwtTokenProvider jwtTokenProvider() {
		return new JwtTokenProvider(JwtConfig.builder()
			.header(jwtProperties.getHeader())
			.secret(jwtProperties.getSecret())
			.base64Secret(jwtProperties.getBase64Secret())
			.tokenValidityInSeconds(jwtProperties.getTokenValidityInSeconds())
			.tokenValidityInSecondsForRememberMe(jwtProperties.getTokenValidityInSecondsForRememberMe())
			.anonymousUrls(jwtProperties.getAnonymousUrls())
			.authenticatedUrls(jwtProperties.getAuthenticatedUrls())
			.permitAllUrls(jwtProperties.getPermitAllUrls())
			.build());
	}

	@ConditionalOnMissingBean
	@Bean
	public JwtTokenService jwtTokenService(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
		return new JwtTokenService(authenticationManager, jwtTokenProvider);
	}
}
