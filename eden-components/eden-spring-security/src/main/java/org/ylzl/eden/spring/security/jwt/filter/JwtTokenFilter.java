/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ylzl.eden.spring.security.jwt.filter;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.filter.GenericFilterBean;
import org.ylzl.eden.commons.lang.ObjectUtils;
import org.ylzl.eden.commons.lang.StringConstants;
import org.ylzl.eden.commons.lang.StringUtils;
import org.ylzl.eden.spring.security.core.SecurityConstants;
import org.ylzl.eden.spring.security.jwt.constant.JwtConstants;
import org.ylzl.eden.spring.security.jwt.env.JwtProperties;
import org.ylzl.eden.spring.security.jwt.token.JwtTokenProvider;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * JWT 令牌过滤器
 *
 * @author gyl
 * @since 1.0.0
 */
@Slf4j
public class JwtTokenFilter extends GenericFilterBean {

  private static final String MSG_INVALID_JWT_EXCEPTION = "JWT Token doFilter throws exception: {}";

  private final JwtTokenProvider jwtTokenProvider;

  private final JwtProperties.Authorization properties;

  public JwtTokenFilter(JwtTokenProvider jwtTokenProvider, JwtProperties jwtProperties) {
    this.jwtTokenProvider = jwtTokenProvider;
    this.properties = jwtProperties.getAuthorization();
  }

  /**
   * 过滤请求
   *
   * @param servletRequest 请求对象
   * @param servletResponse 响应对象
   * @param filterChain 过滤链
   */
  @Override
  public void doFilter(
      ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) servletRequest;
    try {
      String token = this.resolveToken(request);
      if (StringUtils.isNotBlank(token)) {
        setAuthentication(token);
      }
    } catch (Exception e) {
      log.error(MSG_INVALID_JWT_EXCEPTION, e.getMessage(), e);
      ((HttpServletResponse) servletResponse).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
    filterChain.doFilter(servletRequest, servletResponse);
  }

  /**
   * 尝试获取令牌
   *
   * @param request 请求
   * @return 令牌
   */
  private String resolveToken(HttpServletRequest request) {
    String authorization = request.getHeader(properties.getHeader());
    if (StringUtils.isNotBlank(authorization)
        && authorization.startsWith(SecurityConstants.BEARER_TOKEN)) {
      return StringUtils.substringAfter(authorization, SecurityConstants.BEARER_TOKEN);
    }
    return null;
  }

  /**
   * 通过尝试解析令牌设置认证信息
   *
   * @param token 令牌
   */
  private void setAuthentication(String token) {
    Claims claims = jwtTokenProvider.parse(token);
    Collection<? extends GrantedAuthority> simpleGrantedAuthorities =
        Arrays.stream(
                ObjectUtils.trimToString(claims.get(JwtConstants.CLAIM_KEY))
                    .split(StringConstants.COMMA))
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
    User user = new User(claims.getSubject(), StringConstants.EMPTY, simpleGrantedAuthorities);
    Authentication authentication =
        new UsernamePasswordAuthenticationToken(user, token, simpleGrantedAuthorities);
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }
}
