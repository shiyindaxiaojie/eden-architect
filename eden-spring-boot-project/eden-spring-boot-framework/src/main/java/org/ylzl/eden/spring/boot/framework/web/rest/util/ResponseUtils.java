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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Http 响应工具集
 *
 * @author gyl
 * @since 1.0.0
 */
@UtilityClass
public final class ResponseUtils {

    public static <X> ResponseEntity<X> wrapOrNotFound(X maybeResponse) {
        return wrapOrNotFound(maybeResponse, null);
    }

    public static <X> ResponseEntity<X> wrapOrNotFound(X maybeResponse, HttpHeaders header) {
        if (maybeResponse == null) {
            return new ResponseEntity<X>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok().headers(header).body(maybeResponse);
    }
}
