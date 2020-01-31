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

package org.ylzl.eden.spring.boot.security.oauth2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.security.AuthenticationManagerConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.ylzl.eden.spring.boot.security.core.DefaultWebSecuirtyConfiguration;
import org.ylzl.eden.spring.boot.security.oauth2.configurer.OAuth2WebSecurityConfigurerAdapter;

import javax.annotation.PostConstruct;

/**
 * OAuth2 WebSecurity 配置
 *
 * @author gyl
 * @since 0.0.1
 */
@AutoConfigureAfter(DefaultWebSecuirtyConfiguration.class)
@AutoConfigureBefore(AuthenticationManagerConfiguration.class)
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@EnableWebSecurity
@Slf4j
@Configuration
public class OAuth2WebSecurityConfiguration {

	private static final String INIT_AUTHENTICATION_MANAGER = "Initializing AuthenticationManager (OAuth2)";

	private static final String EXP_AUTHENTICATION_MANAGER = "Initialize AuthenticationManager (OAuth2) caught exception";

	private static final String MSG_INJECT_AUTHENTICATION_MANAGER = "Inject AuthenticationManager (OAuth2)";

	private final AuthenticationManagerBuilder authenticationManagerBuilder;

	private final UserDetailsService userDetailsService;

	private final PasswordEncoder passwordEncoder;

	public OAuth2WebSecurityConfiguration(AuthenticationManagerBuilder authenticationManagerBuilder, UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
		this.authenticationManagerBuilder = authenticationManagerBuilder;
		this.userDetailsService = userDetailsService;
		this.passwordEncoder = passwordEncoder;
	}

	@ConditionalOnMissingBean
	@Bean
	public WebSecurityConfigurerAdapter webSecurityConfigurerAdapter() {
		return new OAuth2WebSecurityConfigurerAdapter();
	}

	@ConditionalOnMissingBean
	@Bean
	public AuthenticationManager authenticationManager(WebSecurityConfigurerAdapter webSecurityConfigurerAdapter) throws Exception {
		log.debug(MSG_INJECT_AUTHENTICATION_MANAGER);
		return webSecurityConfigurerAdapter.authenticationManagerBean();
	}

	@PostConstruct
	public void init() {
		log.debug(INIT_AUTHENTICATION_MANAGER);
		try {
			authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
		} catch (Exception e) {
			throw new BeanInitializationException(EXP_AUTHENTICATION_MANAGER, e);
		}
	}
}
