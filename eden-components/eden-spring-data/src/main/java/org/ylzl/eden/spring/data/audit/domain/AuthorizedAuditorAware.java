/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ylzl.eden.spring.data.audit.domain;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.ylzl.eden.spring.framework.bootstrap.constant.Globals;
import org.ylzl.eden.spring.framework.web.util.ServletUtils;

import java.util.Optional;

/**
 * 审计处理
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class AuthorizedAuditorAware implements AuditorAware<String> {

	@Override
	public @NotNull Optional<String> getCurrentAuditor() {
		String authorizedUsername = null;
		if (SecurityContextHolder.getContext().getAuthentication() != null) {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication != null) {
				if (authentication.getPrincipal() instanceof UserDetails) {
					UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
					authorizedUsername = springSecurityUser.getUsername();
				} else if (authentication.getPrincipal() instanceof String) {
					authorizedUsername = (String) authentication.getPrincipal();
				}
			}
		} else {
			authorizedUsername = ServletUtils.getRemoteUser();
		}
		return Optional.of(Optional.ofNullable(authorizedUsername).orElse(Globals.ANONYMOUS_USER));
	}
}
