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

package org.ylzl.eden.consistency.task.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 一致性任务状态
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
@Getter
public enum ConsistencyTaskStatus {

	RUNNABLE(0, "就绪"),
	RUNNING(1, "执行中"),
	SUCCESS(2, "执行成功"),
	FAILED(3, "执行失败");

	private final int code;

	private final String name;
}
