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

package org.ylzl.eden.data.auditor;

import org.ylzl.eden.data.auditor.differ.Diff;

/**
 * 数据比对
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public interface DataDiffer {

	/**
	 * 获取实际比对工具
	 *
	 * @return 比对工具
	 */
	Object getDiffer();

	/**
	 * 比对
	 *
	 * @param oldVersion 历史版本
	 * @param currentVersion 当前版本
	 * @return 比对结果
	 */
	Diff compare(Object oldVersion, Object currentVersion);
}
