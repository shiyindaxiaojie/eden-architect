/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ylzl.eden.spring.framework.beans.util;

import lombok.experimental.UtilityClass;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.lang.reflect.Field;
import java.util.Collections;

/**
 * MultiValueMap 工具集
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@UtilityClass
public class MultiValueMapUtils {

	public static MultiValueMap<String, Object> toMap(Object obj) {
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
		Field[] fields = obj.getClass().getDeclaredFields();
		int i = 0;
		for (int len = fields.length; i < len; ++i) {
			String varName = fields[i].getName();
			boolean accessFlag = fields[i].isAccessible();
			fields[i].setAccessible(true);
			Object o;
			try {
				o = fields[i].get(obj);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
			if (o != null) {
				map.put(varName, Collections.singletonList(o.toString()));
			}
			fields[i].setAccessible(accessFlag);
		}
		return map;
	}
}
