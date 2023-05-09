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

package org.ylzl.eden.spring.boot.bootstrap.util;

import lombok.experimental.UtilityClass;
import org.springframework.boot.SpringApplication;
import org.ylzl.eden.spring.framework.bootstrap.constant.SpringProfiles;
import org.ylzl.eden.spring.framework.bootstrap.constant.SpringProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Spring Profile 工具类
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@UtilityClass
public class SpringBootProfileUtils {

	public static void addDefaultProfile(SpringApplication app) {
		Map<String, Object> defProperties = new HashMap<>();
		defProperties.put(SpringProperties.SPRING_PROFILE_DEFAULT, SpringProfiles.SPRING_PROFILE_DEVELOPMENT);
		app.setDefaultProperties(defProperties);
	}
}
