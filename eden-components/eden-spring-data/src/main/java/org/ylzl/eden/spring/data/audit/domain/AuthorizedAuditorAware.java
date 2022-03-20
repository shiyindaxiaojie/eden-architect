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

package org.ylzl.eden.spring.data.audit.domain;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.ylzl.eden.spring.framework.core.constant.GlobalConstants;
import org.ylzl.eden.spring.framework.core.util.SpringAssert;
import org.ylzl.eden.spring.framework.web.util.RequestContextHolderUtils;

import java.util.Optional;

/**
 * 认证的审计装饰器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.0.0
 */
public class AuthorizedAuditorAware implements AuditorAware<String> {

	@Override
	public Optional<String> getCurrentAuditor() {
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
			RequestContextHolderUtils.getRemoteUser();
			authorizedUsername = RequestContextHolderUtils.getRemoteUser();
		}
		SpringAssert.notNull(authorizedUsername);
		return Optional.of(Optional.of(authorizedUsername).orElse(GlobalConstants.SYSTEM));
	}
}
