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

package org.ylzl.eden.spring.boot.security.oauth2.resource;

import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.ylzl.eden.spring.boot.security.core.SecurityConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 权限提取器
 *
 * @author gyl
 * @since 0.0.1
 */
public class SimpleAuthoritiesExtractor implements AuthoritiesExtractor {

    private final String oauth2AuthoritiesAttribute;

    public SimpleAuthoritiesExtractor(String oauth2AuthoritiesAttribute) {
        this.oauth2AuthoritiesAttribute = oauth2AuthoritiesAttribute;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<GrantedAuthority> extractAuthorities(Map<String, Object> map) {
        List<String> oauth2Authorities = (List<String>) map.get(oauth2AuthoritiesAttribute);
        if (oauth2Authorities.isEmpty()) {
            oauth2Authorities = Collections.singletonList(SecurityConstants.ROLE_USER);
        }
        List<GrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();
        for (String oauth2Authority : oauth2Authorities) {
            simpleGrantedAuthorities.add(new SimpleGrantedAuthority(oauth2Authority));
        }
        return simpleGrantedAuthorities;
    }
}
