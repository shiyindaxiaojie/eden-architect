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

package org.ylzl.eden.spring.framework.bootstrap.util;

import lombok.experimental.UtilityClass;
import org.springframework.core.env.Environment;
import org.ylzl.eden.commons.lang.ArrayUtils;

/**
 * Spring Profile 工具类
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@UtilityClass
public class SpringProfileUtils {

	public static String[] getActiveProfiles(Environment env) {
		String[] profiles = env.getActiveProfiles();
		if (ArrayUtils.isEmpty(profiles)) {
			String[] defaults = env.getDefaultProfiles();
			if (ArrayUtils.isEmpty(defaults)) {
				return new String[0];
			}
			return defaults;
		}
		return profiles;
	}
}
