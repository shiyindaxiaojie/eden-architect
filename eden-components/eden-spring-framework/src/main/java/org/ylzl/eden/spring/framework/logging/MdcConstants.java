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

package org.ylzl.eden.spring.framework.logging;

import lombok.experimental.UtilityClass;

/**
 * MDC 常量定义
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@UtilityClass
public class MdcConstants {

	public static final String APP = "app";

	public static final String PROFILE = "profile";

	public static final String CLASS_NAME = "className";

	public static final String METHOD_NAME = "methodName";

	public static final String ARGUMENTS = "arguments";

	public static final String RETURN_VALUE = "returnValue";

	public static final String DURATION = "duration";

	public static final String REQUEST_URI = "requestURI";

	public static final String REMOTE_USER = "remoteUser";

	public static final String REMOTE_ADDR = "remoteAddr";

	public static final String LOCAL_ADDR = "localAddr";
}
