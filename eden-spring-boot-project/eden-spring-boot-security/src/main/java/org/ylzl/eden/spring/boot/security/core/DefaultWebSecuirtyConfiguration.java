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

package org.ylzl.eden.spring.boot.security.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Web 安全配置
 *
 * @author gyl
 * @since 1.0.0
 */
@Slf4j
@Configuration
public class DefaultWebSecuirtyConfiguration {

  private static final String MSG_AUTOWIRED_PASSWORD_ENCODER =
      "Autowired PasswordEncoder (BCryptPasswordEncoder)";

  private static final String MSG_AUTOWIRED_USER_DETAILS_SERVICE =
      "Autowired UserDetailsService (InMemoryUserDetailsManager)";

  private final SecurityProperties securityProperties;

  public DefaultWebSecuirtyConfiguration(SecurityProperties securityProperties) {
    this.securityProperties = securityProperties;
  }

  @ConditionalOnMissingBean
  @Bean
  public PasswordEncoder passwordEncoder() {
    log.debug(MSG_AUTOWIRED_PASSWORD_ENCODER);
    return new BCryptPasswordEncoder();
  }

  @ConditionalOnMissingBean
  @Bean
  public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
    log.debug(MSG_AUTOWIRED_USER_DETAILS_SERVICE);
    SecurityProperties.User user = securityProperties.getUser();
    List<SimpleGrantedAuthority> authorities = new ArrayList<>();
    List<String> roles = user.getRoles();
    for (String role : roles) {
      authorities.add(new SimpleGrantedAuthority(role));
    }
    UserDetails userDetails =
        new User(user.getName(), passwordEncoder.encode(user.getPassword()), authorities);
    return new InMemoryUserDetailsManager(Collections.singletonList(userDetails));
  }
}
