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

package org.ylzl.eden.spring.security.access;

import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 角色访问决策适配器
 *
 * @author gyl
 * @since 2.4.x
 */
public class RoleAccessDecisionVoter implements AccessDecisionVoter<Object> {

	// 这里的需要从DB加载
	private final Map<String, String> urlRoleMap =
		new HashMap<String, String>() {
			{
				put("/open/**", "ROLE_ANONYMOUS");
				put("/health", "ROLE_ANONYMOUS");
				put("/restart", "ADMIN");
				put("/demo", "ROLE_USER");
			}
		};

	@Override
	public boolean supports(ConfigAttribute attribute) {
		return true;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}

	@Override
	public int vote(
		Authentication authentication, Object object, Collection<ConfigAttribute> attributes) {
		if (authentication == null) {
			return ACCESS_DENIED;
		}

		FilterInvocation fi = (FilterInvocation) object;
		String url = fi.getRequestUrl();
    /*for(Map.Entry<String,String> entry:urlRoleMap.entrySet()){
        if(antPathMatcher.match(entry.getKey(), url)){
            return SecurityConfig.createList(entry.getValue());
        }
    }*/

		int result = ACCESS_ABSTAIN;
		Collection<? extends GrantedAuthority> authorities = extractAuthorities(authentication);

		for (ConfigAttribute attribute : attributes) {
			if (attribute.getAttribute() == null) {
				continue;
			}
			if (this.supports(attribute)) {
				result = ACCESS_DENIED;
				for (GrantedAuthority authority : authorities) {
					if (attribute.getAttribute().equals(authority.getAuthority())) {
						return ACCESS_GRANTED;
					}
				}
			}
		}

		return result;
	}

	private Collection<? extends GrantedAuthority> extractAuthorities(Authentication authentication) {
		return authentication.getAuthorities();
	}
}
