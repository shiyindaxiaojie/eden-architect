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
package org.ylzl.eden.spring.boot.commons.env;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.ylzl.eden.spring.boot.commons.env.browser.BrowserEnum;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 浏览器工具集
 *
 * @author gyl
 * @since 0.0.1
 */
@UtilityClass
public class BrowserUtils {

    public static boolean isIE(@NonNull HttpServletRequest request) {
        BrowserEnum browserEnum = BrowserEnum.toBrowserEnum(request);
        switch (browserEnum) {
            case IE6:
            case IE7:
            case IE8:
            case IE9:
            case IE10:
            case IE11:
                return true;
        }
        return false;
    }

    public static String getLanguage(@NonNull HttpServletRequest request) {
        return request.getLocale().getLanguage();
    }

    public static String resolveValue(@NonNull String value) throws UnsupportedEncodingException {
        return URLEncoder.encode(value, String.valueOf(CharsetConstants.UTF_8));
    }
}
