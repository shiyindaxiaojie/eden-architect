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

package org.ylzl.eden.spring.framework.bootstrap.constant;

import lombok.experimental.UtilityClass;
import org.ylzl.eden.commons.env.CharsetConstants;

/**
 * 全局常量定义
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@UtilityClass
public final class GlobalConstants {

	/**
	 * 默认编码
	 */
	public static final String DEFAULT_ENCODING = CharsetConstants.UTF_8_NAME;

	/**
	 * 默认语言
	 */
	public static final String DEFAULT_LANGUAGE = "zh-cn";

	/**
	 * 默认字体
	 */
	public static final String DEFAULT_FONT_NAME = "simsun";

	/**
	 * 系统用户
	 */
	public static final String SYSTEM = "system";

	/**
	 * 匿名用户
	 */
	public static final String ANONYMOUS_USER = "anonymoususer";
}
