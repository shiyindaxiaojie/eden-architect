package org.ylzl.eden.spring.security.jwt.filter;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.GenericFilterBean;
import org.ylzl.eden.spring.framework.error.ClientErrorType;
import org.ylzl.eden.spring.framework.web.util.ResponseUtils;
import org.ylzl.eden.spring.security.jwt.token.JwtTokenProvider;
import org.ylzl.eden.spring.security.core.constant.AuthenticationConstants;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * JWT 过滤器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {

	private final JwtTokenProvider jwtTokenProvider;

	private final PathMatcher pathMatcher;

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		if (!isAnonymousUrls(request)) {
			String jwt = resolveToken(request);
			try {
				ClientErrorType.notNull(jwt,"A0220");
				this.jwtTokenProvider.validateToken(jwt);
			} catch (Exception e) {
				ResponseUtils.wrap(response, HttpServletResponse.SC_UNAUTHORIZED,"A0220", e.getMessage());
				return;
			}
			Authentication authentication = this.jwtTokenProvider.getAuthentication(jwt);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		filterChain.doFilter(servletRequest, servletResponse);
	}

	private String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader(jwtTokenProvider.getConfig().getHeader());
		if (StringUtils.isNotBlank(bearerToken) && bearerToken.startsWith(AuthenticationConstants.BEARER_PREFIX)) {
			return bearerToken.substring(AuthenticationConstants.BEARER_PREFIX.length());
		}
		return null;
	}

	private boolean isAnonymousUrls(HttpServletRequest request) {
		List<String> anonymousUrls = jwtTokenProvider.getConfig().getAnonymousUrls();
		if (CollectionUtils.isEmpty(anonymousUrls)) {
			return false;
		}
		String requestURI = request.getRequestURI();
		return anonymousUrls.stream().anyMatch(url -> pathMatcher.match(url, requestURI));
	}
}
