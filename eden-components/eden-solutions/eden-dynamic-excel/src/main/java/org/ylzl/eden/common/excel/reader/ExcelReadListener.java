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

package org.ylzl.eden.common.excel.reader;

import org.ylzl.eden.common.excel.model.ValidationErrors;

import java.util.List;

/**
 * Excel 读取事件监听
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public interface ExcelReadListener<T> {

	/**
	 * 每读取一行调用一次
	 *
	 * @param data 读取到的数据
	 */
	void read(Object data);

	/**
	 * 读取完成
	 */
	void finish();

	/**
	 * 根据批次大小读取相应的数据
	 *
	 * @return 数据
	 */
	List<T> getBatchData();

	/**
	 * 设置批次大小
	 *
	 * @param batchSize 批次大小
	 */
	void setBatchSize(int batchSize);

	/**
	 * 获取所有校验错误信息
	 *
	 * @return 错误信息清单
	 */
	List<ValidationErrors> getErrors();
}
