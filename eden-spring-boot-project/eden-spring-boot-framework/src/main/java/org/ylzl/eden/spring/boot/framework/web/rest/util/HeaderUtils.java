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
package org.ylzl.eden.spring.boot.framework.web.rest.util;

import lombok.experimental.UtilityClass;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;

import java.text.MessageFormat;

/**
 * Http 头信息工具集
 *
 * @author gyl
 * @since 0.0.1
 */
@UtilityClass
public final class HeaderUtils {

    private static final String HEADER_NAME_X_MESSAGE = "X-{0}-message";

    private static final String HEADER_NAME_X_PARAMS = "X-{0}-params";

    private static final String HEADER_NAME_X_ERROR = "X-{0}-error";

    private static final String HEADER_NAME_X_ENTITY_CREATION = "{0}.{1}.created";

    private static final String HEADER_NAME_X_ENTITY_UPDATE = "{0}.{1}.updated";

    private static final String HEADER_NAME_X_ENTITY_DELETION = "{0}.{1}.deleted";

    public static HttpHeaders create(String appName, String message, String param) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(MessageFormat.format(HEADER_NAME_X_MESSAGE, appName), message);
        headers.add(MessageFormat.format(HEADER_NAME_X_PARAMS, appName), param);
        return headers;
    }

    public static HttpHeaders createEntityCreation(String appName, String entityName, String param) {
        return create(appName, MessageFormat.format(HEADER_NAME_X_ENTITY_CREATION, appName, entityName), param);
    }

    public static HttpHeaders createEntityUpdate(String appName, String entityName, String param) {
        return create(appName, MessageFormat.format(HEADER_NAME_X_ENTITY_UPDATE, appName, entityName), param);
    }

    public static HttpHeaders createEntityDeletion(String appName, String entityName, String param) {
        return create(appName, MessageFormat.format(HEADER_NAME_X_ENTITY_DELETION, appName, entityName), param);
    }

    public static HttpHeaders createFailure(String appName, String message, String param, String error) {
        HttpHeaders headers = create(appName, message, param);
        headers.add(MessageFormat.format(HEADER_NAME_X_ERROR, appName), error);
        return headers;
    }
}
