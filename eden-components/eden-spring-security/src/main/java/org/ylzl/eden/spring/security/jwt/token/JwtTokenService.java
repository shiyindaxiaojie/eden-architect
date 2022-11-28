package org.ylzl.eden.spring.security.jwt.token;

import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.ylzl.eden.commons.lang.StringConstants;
import org.ylzl.eden.spring.framework.error.http.UnauthorizedException;
import org.ylzl.eden.spring.security.jwt.constant.JwtConstants;
import org.ylzl.eden.spring.security.jwt.model.AccessToken;
import org.ylzl.eden.spring.security.jwt.userdetails.LoginUserDetails;

import java.util.Map;

/**
 * JWT 令牌验证服务
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
@Slf4j
public class JwtTokenService {

	private static final String AUTHENTICATE_BAD_CREDENTIALS = "JWT authenticated failed due to bad credentials：{}";

	private static final String AUTHENTICATE_EXCEPTION = "JWT authenticated failed, caught exception: {}";

	private final AuthenticationManager authenticationManager;

	private final JwtTokenProvider jwtTokenProvider;

	/**
	 * 认证
	 *
	 * @param login
	 * @param claims
	 * @return
	 */
	public AccessToken authenticate(LoginUserDetails login, Map<String, Object> claims) {
		try {
			UsernamePasswordAuthenticationToken authenticationToken =
				new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword());
			Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
			SecurityContextHolder.getContext().setAuthentication(authentication);

			if (authentication.getAuthorities() != null) {
				if (claims == null) {
					claims = Maps.newHashMap();
				}
				StringBuilder authorities = new StringBuilder();
				for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
					authorities.append(grantedAuthority.getAuthority()).append(StringConstants.COMMA);
				}
				authorities.deleteCharAt(authorities.length() - 1);
				claims.put(JwtConstants.AUTHORITIES_KEY, authorities);
			}

			return jwtTokenProvider.createToken(authentication, login.isRememberMe(), claims);
		} catch (BadCredentialsException ex) {
			log.error(AUTHENTICATE_BAD_CREDENTIALS, ex.getMessage(), ex);
			throw new UnauthorizedException(ex.getMessage());
		} catch (Exception ex) {
			log.error(AUTHENTICATE_EXCEPTION, ex.getMessage(), ex);
			throw ex;
		}
	}
}
