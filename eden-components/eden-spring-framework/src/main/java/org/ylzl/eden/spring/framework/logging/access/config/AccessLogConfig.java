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

package org.ylzl.eden.spring.framework.logging.access.config;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 日志切面配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@EqualsAndHashCode
@ToString
@Setter
@Getter
public class AccessLogConfig {

	/* 是否保存到 MDC */
	private boolean enabledMdc = true;

	/* 需要输出日志的包名 */
	private String expression;

	/* 日志输出采样率 */
	private double sampleRate = 1.0;

	/* 是否输出入参 */
	private boolean logArguments = true;

	/* 是否输出返回值 */
	private boolean logReturnValue = true;

	/* 是否输出方法执行耗时 */
	private boolean logExecutionTime = true;

	/* 最大长度 */
	private int maxLength = 500;

	/* 慢日志 */
	private long slowThreshold = 1000;
}
