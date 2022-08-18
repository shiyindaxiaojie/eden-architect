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

package org.ylzl.eden.spring.framework.web.util;

import lombok.experimental.UtilityClass;
import org.ylzl.eden.commons.lang.ObjectUtils;
import org.ylzl.eden.commons.lang.StringConstants;

import javax.servlet.ServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Servlet 工具类
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@UtilityClass
public final class ServletUtils {

	public static Map<String, String> toMap(ServletRequest request) {
		Map<String, String[]> parameterMap = request.getParameterMap();
		Map<String, String> returnMap = new HashMap<String, String>();
		for (Entry<String, String[]> entry : parameterMap.entrySet()) {
			Object valueObj = entry.getValue();
			if (ObjectUtils.isEmpty(valueObj)) {
				returnMap.put(entry.getKey(), StringConstants.EMPTY);
			} else if (valueObj instanceof String[]) {
				String[] values = (String[]) valueObj;
				StringBuilder sb = new StringBuilder();
				for (String val : values) {
					sb.append(val).append(StringConstants.DOT);
				}
				if (sb.indexOf(StringConstants.DOT) >= 0) {
					sb.delete(sb.length() - 1, sb.length());
				}
				returnMap.put(entry.getKey(), sb.toString());
			} else {
				returnMap.put(entry.getKey(), ObjectUtils.trimToString(valueObj));
			}
		}
		return returnMap;
	}
}
