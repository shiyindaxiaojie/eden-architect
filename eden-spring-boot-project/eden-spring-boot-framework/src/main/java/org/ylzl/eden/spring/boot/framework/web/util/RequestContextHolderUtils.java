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

package org.ylzl.eden.spring.boot.framework.web.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.ylzl.eden.spring.boot.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Spring 请求上下文工具
 *
 * @author gyl
 * @since 0.0.1
 */
public class RequestContextHolderUtils {

    private RequestContextHolderUtils() {
    }

    public static ServletRequestAttributes getServletRequestAttributes() {
        return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    }

    public static HttpServletRequest getRequest() {
        return getServletRequestAttributes().getRequest();
    }

    public static HttpServletResponse getResponse() {
        return getServletRequestAttributes().getResponse();
    }

    public static HttpSession getSession() {
        return getRequest().getSession();
    }

    public static String getRemoteUser() {
        return StringUtils.trimToEmpty(getRequest().getRemoteUser());
    }

    public static String getRequestURI() {
        return getRequest().getRequestURI();
    }

    public static String getContextPath() {
        return getRequest().getContextPath();
    }

    public static String getQueryString() {
        return getRequest().getQueryString();
    }

    public static String getRequestPath() {
        HttpServletRequest request = getRequest();
        String queryString = request.getQueryString();
        String requestURI = request.getRequestURI();
        if (StringUtils.isNotEmpty(queryString)) {
            requestURI += "?" + queryString;
        }
        int index = requestURI.indexOf("&");
        if (index > -1) {
            requestURI = requestURI.substring(0, index);
        }
        return requestURI.substring(request.getContextPath().length() + 1);
    }

    public static boolean isAjaxRequest() {
        HttpServletRequest request = getRequest();
        String accept = request.getHeader("accept");
        if (accept != null && accept.indexOf("application/json") != -1) {
            return true;
        }
        String xRequestedWith = request.getHeader("X-Requested-With");
        if (xRequestedWith != null && xRequestedWith.indexOf("XMLHttpRequest") != -1) {
            return true;
        }
        return false;
    }
}
