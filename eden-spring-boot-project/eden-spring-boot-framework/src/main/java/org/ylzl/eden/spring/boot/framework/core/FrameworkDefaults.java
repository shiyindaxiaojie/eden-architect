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

package org.ylzl.eden.spring.boot.framework.core;

import lombok.experimental.UtilityClass;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpMethod;
import org.ylzl.eden.spring.boot.framework.core.FrameworkProperties.Http.Version;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 框架默认属性值
 *
 * @author gyl
 * @since 0.0.1
 */
@UtilityClass
public final class FrameworkDefaults {

	public static class Cors {

		public static final List<String> allowedOrigins = Collections.singletonList("*");

		public static final List<String> allowedMethods = Collections.singletonList("*");

		public static final List<HttpMethod> resolvedMethods = Arrays.asList(new HttpMethod[] { HttpMethod.GET, HttpMethod.POST});

		public static final List<String> allowedHeaders = Collections.singletonList("*");

		public static final List<String> exposedHeaders = Arrays.asList(new String[]{"Authorization", "Link", "X-Total-Count"});

		public static final Boolean allowCredentials = true;

		public static final Long maxAge = 1800L;
	}

    public static class Http {

        public static final Version version = Version.V_1_1;

        public static final boolean useUndertowUserCipherSuitesOrder = true;

        public static class Cache {

            public static final int timeToLiveInDays = 365;
        }
    }
}
