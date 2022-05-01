package org.ylzl.eden.spring.security.jwt.token;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.ylzl.eden.commons.lang.StringConstants;
import org.ylzl.eden.spring.framework.error.http.UnauthorizedException;
import org.ylzl.eden.spring.security.jwt.config.JwtConfig;
import org.ylzl.eden.spring.security.jwt.constant.JwtConstants;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

/**
 * JWT 令牌提供器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 1.0.0
 */
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider implements InitializingBean {

	private static final String INVALID_TOKEN = "Invalid JWT token, caught exception: {}";

	private final JwtConfig jwtConfig;

	private JwtParser jwtParser;

	private Key key;

	private long tokenValidityInMilliseconds;

	private long tokenValidityInMillisecondsForRememberMe;

	@Override
	public void afterPropertiesSet() {
		byte[] keyBytes = jwtConfig.getBase64Secret() != null ?
			Decoders.BASE64.decode(jwtConfig.getBase64Secret()) :
		    jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8);
		key = Keys.hmacShaKeyFor(keyBytes);
		this.jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
		this.tokenValidityInMilliseconds = 1000 * jwtConfig.getTokenValidityInSeconds();
		this.tokenValidityInMillisecondsForRememberMe = 1000 * jwtConfig.getTokenValidityInSecondsForRememberMe();
	}

	public String createToken(Authentication authentication, boolean rememberMe, Map<String, Object> claims) {
		long now = (new Date()).getTime();
		Date expiration;
		if (rememberMe) {
			expiration = new Date(now + this.tokenValidityInMillisecondsForRememberMe);
		} else {
			expiration = new Date(now + this.tokenValidityInMilliseconds);
		}

		if (CollectionUtils.isNotEmpty(authentication.getAuthorities())) {
			String authorities = authentication.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority).collect(Collectors.joining(StringConstants.COMMA));
			claims.put(JwtConstants.AUTHORITIES_KEY, authorities);
		}

		return Jwts
			.builder()
			.setSubject(authentication.getName())
			.addClaims(claims)
			.signWith(key, SignatureAlgorithm.HS512)
			.setExpiration(expiration)
			.compact();
	}

	public void validateToken(String token) {
		try {
			jwtParser.parseClaimsJws(token);
		} catch (ExpiredJwtException e) {
			log.debug(e.getMessage(), e);
			throw new UnauthorizedException("令牌已失效");
		} catch (UnsupportedJwtException e) {
			log.debug(e.getMessage(), e);
			throw new UnauthorizedException("不支持的令牌");
		} catch (MalformedJwtException e) {
			log.debug(e.getMessage(), e);
			throw new UnauthorizedException("令牌格式错误");
		} catch (SecurityException e) {
			log.debug(e.getMessage(), e);
			throw new UnauthorizedException("校验令牌不通过");
		} catch (IllegalArgumentException e) {
			log.error(e.getMessage(), e);
			throw new UnauthorizedException("校验令牌异常");
		}
	}

	public Authentication getAuthentication(String token) {
		Claims claims = parseClaims(token);

		Collection<? extends GrantedAuthority> authorities = Collections.emptyList();
		if (claims.containsKey(JwtConstants.AUTHORITIES_KEY)) {
			authorities = Arrays
				.stream(claims.get(JwtConstants.AUTHORITIES_KEY).toString().split(StringConstants.COMMA))
				.filter(auth -> !auth.trim().isEmpty())
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());
		}

		User principal = new User(claims.getSubject(), StringConstants.EMPTY, authorities);
		return new UsernamePasswordAuthenticationToken(principal, token, authorities);
	}

	public Claims parseClaims(String token) {
		return jwtParser.parseClaimsJws(token).getBody();
	}

	public JwtConfig getConfig() {
		return jwtConfig;
	}
}
