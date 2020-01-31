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

package org.ylzl.eden.spring.boot.security.core.util;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.ylzl.eden.spring.boot.security.core.SecurityConstants;
import org.ylzl.eden.spring.boot.security.core.enums.AuthenticationTypeEnum;

/**
 * Spring Security 工具集
 *
 * @author gyl
 * @since 0.0.1
 */
@UtilityClass
public final class SpringSecurityUtils {

    public static Authentication getAuthentication() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        if (securityContext.getAuthentication() != null) {
            return securityContext.getAuthentication();
        }
        return null;
    }

    public static String getAuthorizationHeader() {
        Authentication authentication = getAuthentication();
        if (authentication != null) {
            if (authentication.getDetails() instanceof OAuth2AuthenticationDetails) {
                OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();
                return getAuthorizationHeader(details);
            }
            if (authentication.getCredentials() instanceof String) {
                return getAuthorizationHeader((String) authentication.getCredentials());
            }
        }
        return null;
    }

    public static String getAuthorizationHeader(@NonNull OAuth2AuthenticationDetails details) {
        return AuthenticationTypeEnum.BEARER_TOKEN.getAuthorization(details.getTokenValue());
    }

    public static String getAuthorizationHeader(@NonNull String credentials) {
        return AuthenticationTypeEnum.BEARER_TOKEN.getAuthorization(credentials);
    }

    public static String getCurrentUserLogin() {
        Authentication authentication = getAuthentication();
        if (authentication != null) {
            if (authentication.getPrincipal() instanceof UserDetails) {
                UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
                return springSecurityUser.getUsername();
            }
            if (authentication.getPrincipal() instanceof String) {
                return (String) authentication.getPrincipal();
            }
        }
        return null;
    }

    public static String getCurrentUserCredentials() {
        Authentication authentication = getAuthentication();
        if (authentication != null) {
            if (authentication.getDetails() instanceof OAuth2AuthenticationDetails) {
                OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();
                return details.getTokenValue();
            }
            if (authentication.getCredentials() instanceof String) {
                return (String) authentication.getCredentials();
            }
        }
        return null;
    }

    public static boolean isAuthenticated() {
        Authentication authentication = getAuthentication();
        if (authentication != null) {
            for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
                if (SecurityConstants.ROLE_ANONYMOUS.equals(grantedAuthority.getAuthority())) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public static boolean isCurrentUserHasAuthority(@NonNull String authority) {
        Authentication authentication = getAuthentication();
        if (authentication != null) {
            for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
                if (grantedAuthority.getAuthority().equals(authority)) {
                    return true;
                }
            }
        }
        return false;
    }
}
