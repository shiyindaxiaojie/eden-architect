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

package org.ylzl.eden.spring.boot.security.jwt.configurer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;
import org.ylzl.eden.spring.boot.framework.core.FrameworkConstants;
import org.ylzl.eden.spring.boot.security.jwt.JwtProperties;
import org.ylzl.eden.spring.boot.security.jwt.token.JwtTokenProvider;

/**
 * Jwt WebSecurity 配置适配器
 *
 * @author gyl
 * @since 1.0.0
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class JwtWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

	@Value(FrameworkConstants.NAME_PATTERN)
	private String applicationName;

	private AuthenticationEntryPoint authenticationEntryPoint;

	private CorsFilter corsFilter;

    private final JwtTokenProvider jwtTokenProvider;

    private final JwtProperties jwtProperties;

    public JwtWebSecurityConfigurerAdapter(JwtTokenProvider jwtTokenProvider, JwtProperties jwtProperties) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.jwtProperties = jwtProperties;
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.httpBasic().realmName(applicationName)
			.and()
			.csrf()
            .disable()
            .headers()
            .frameOptions()
            .disable()
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .apply(jwtSecurityConfigurer());

        if (authenticationEntryPoint != null) {
			http.exceptionHandling()
				.authenticationEntryPoint(authenticationEntryPoint);
		}

        if (corsFilter != null) {
			http.addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class);
		}
    }

    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

	@Autowired(required = false)
	public void setAuthenticationEntryPoint(AuthenticationEntryPoint authenticationEntryPoint) {
		this.authenticationEntryPoint = authenticationEntryPoint;
	}

	@Autowired(required = false)
	public void setCorsFilter(CorsFilter corsFilter) {
		this.corsFilter = corsFilter;
	}

	protected JwtSecurityConfigurer jwtSecurityConfigurer() {
        return new JwtSecurityConfigurer(jwtTokenProvider, jwtProperties);
    }
}
