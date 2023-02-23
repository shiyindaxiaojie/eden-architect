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

package org.ylzl.eden.common.excel;

import org.ylzl.eden.common.excel.reader.ExcelReadListener;

import java.io.File;
import java.io.InputStream;

/**
 * Excel 读取接口
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public interface ExcelReader {

	/**
	 * 读取 Excel
	 *
	 * @param inputStream 输入流
	 * @param head 标题映射
	 * @param excelReadListener 读取监听器
	 * @see org.ylzl.eden.common.excel.reader.PersistenceExcelReadListener
	 */
	void read(InputStream inputStream, Class<?> head, ExcelReadListener<?> excelReadListener);

	/**
	 * 读取 Excel
	 *
	 * @param file 文件
	 * @param head 标题映射
	 * @param excelReadListener 读取监听器
	 * @see org.ylzl.eden.common.excel.reader.PersistenceExcelReadListener
	 */
	void read(File file, Class<?> head, ExcelReadListener<?> excelReadListener);
}
