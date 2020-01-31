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

import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;

import java.util.Map;

/**
 * 用户提取器
 *
 * @author gyl
 * @since 0.0.1
 */
public class SimplePrincipalExtractor implements PrincipalExtractor {

    public static final String DEFAULT_VALUE = "unknown";

    private final String oauth2PrincipalAttribute;

    public SimplePrincipalExtractor(String oauth2PrincipalAttribute) {
        this.oauth2PrincipalAttribute = oauth2PrincipalAttribute;
    }

    @Override
    public Object extractPrincipal(Map<String, Object> map) {
        if (map.containsKey(oauth2PrincipalAttribute)) {
            return map.get(oauth2PrincipalAttribute);
        }
        return DEFAULT_VALUE;
    }
}
