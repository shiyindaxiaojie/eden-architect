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

package org.ylzl.eden.spring.boot.integration.swagger;

import lombok.experimental.UtilityClass;
import lombok.NoArgsConstructor;

/**
 * Swagger 配置属性默认值
 *
 * @author gyl
 * @since 0.0.1
 */
@UtilityClass
public final class SwaggerDefaults {

    public static final String contactEmail = "1813986321@qq.com";

    public static final String contactName = "郭远陆";

    public static final String contactUrl = null;

    public static final String defaultIncludePattern = "/api/.*";

    public static final String description = "API documentation";

    public static final String host = null;

    public static final String license = null;

    public static final String licenseUrl = null;

    public static final String[] protocols = {};

    public static final String termsOfServiceUrl = null;

    public static final String title = "Application API";

    public static final boolean useDefaultResponseMessages = true;

    public static final String version = "1.0.0.RELEASE";
}
