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

package org.ylzl.eden.spring.boot.security.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.ylzl.eden.spring.boot.security.core.DefaultWebSecuirtyConfiguration;
import org.ylzl.eden.spring.boot.security.jwt.configurer.JwtWebSecurityConfigurerAdapter;
import org.ylzl.eden.spring.boot.security.jwt.token.JwtTokenProvider;

import javax.annotation.PostConstruct;

/**
 * JWT WebSecurity 配置
 *
 * @author gyl
 * @since 1.0.0
 */
@AutoConfigureAfter(DefaultWebSecuirtyConfiguration.class)
@Slf4j
@Configuration
public class JwtWebSecurityConfiguration {

/*	private static final String INIT_AUTHENTICATION_MANAGER = "Initializing AuthenticationManager (JWT)";

	private static final String EXP_AUTHENTICATION_MANAGER = "Initialize AuthenticationManager (JWT) caught exception";*/

	private static final String MSG_INJECT_AUTHENTICATION_MANAGER = "Inject AuthenticationManager (JWT)";

/*    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final UserDetailsService userDetailsService;

	private final PasswordEncoder passwordEncoder;

	public JwtWebSecurityConfiguration(AuthenticationManagerBuilder authenticationManagerBuilder, UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
		this.authenticationManagerBuilder = authenticationManagerBuilder;
		this.userDetailsService = userDetailsService;
		this.passwordEncoder = passwordEncoder;
	}*/

    @ConditionalOnMissingBean
    @Bean
    public WebSecurityConfigurerAdapter webSecurityConfigurerAdapter(JwtTokenProvider jwtTokenProvider, JwtProperties jwtProperties) {
        return new JwtWebSecurityConfigurer(jwtTokenProvider, jwtProperties);
    }

    @ConditionalOnMissingBean
    @Bean
    public AuthenticationManager authenticationManager(WebSecurityConfigurerAdapter webSecurityConfigurerAdapter) throws Exception {
		log.debug(MSG_INJECT_AUTHENTICATION_MANAGER);
        return webSecurityConfigurerAdapter.authenticationManagerBean();
    }

	/*@PostConstruct
	public void init() {
		log.debug(INIT_AUTHENTICATION_MANAGER);
		try {
			authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
		} catch (Exception e) {
			throw new BeanInitializationException(EXP_AUTHENTICATION_MANAGER, e);
		}
	}*/

	protected static class JwtWebSecurityConfigurer extends JwtWebSecurityConfigurerAdapter {

        public JwtWebSecurityConfigurer(JwtTokenProvider jwtTokenProvider, JwtProperties jwtProperties) {
            super(jwtTokenProvider, jwtProperties);
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
            super.configure(http);

            http.authorizeRequests()
				.antMatchers(JwtConstants.ENDPOINT_TOKEN).permitAll()
				.anyRequest().authenticated();
        }
    }
}
