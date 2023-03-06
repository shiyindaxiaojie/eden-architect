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

import java.io.File;
import java.io.OutputStream;
import java.util.List;

/**
 * Excel 写入接口
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public interface ExcelWriter {

	/**
	 * 写入 Excel
	 *
	 * @param outputStream 输出流
	 * @param data 填充数据
	 * @param head 标题
	 */
	void write(OutputStream outputStream, List<Object> data, Class<?> head);

	/**
	 * 写入 Excel
	 *
	 * @param file 文件
	 * @param data 填充数据
	 * @param head 标题
	 */
	void write(File file, List<Object> data, Class<?> head);
}
