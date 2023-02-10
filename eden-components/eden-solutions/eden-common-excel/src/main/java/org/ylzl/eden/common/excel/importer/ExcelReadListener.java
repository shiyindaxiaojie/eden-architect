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

package org.ylzl.eden.common.excel.importer;

import org.ylzl.eden.common.excel.model.ValidationErrors;

import java.util.List;

/**
 * TODO
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public interface ExcelReadListener<T> {

	/**
	 * 每读取一行调用一次
	 *
	 * @param data 读取到的数据
	 * @param context 读取上下文
	 */
	void read(T data, ExcelReadContext context);

	/**
	 * 获取所有校验错误信息
	 *
	 * @return 错误信息清单
	 */
	List<ValidationErrors> getErrors();
}
