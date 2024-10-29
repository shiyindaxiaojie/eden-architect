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

import java.lang.annotation.*;

/**
 * Excel 导入
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface ExcelImporter {

	/**
	 * 文件名称
	 *
	 * @return 文件名称
	 */
	String fileName() default "file";

	/**
	 * 忽略空行
	 *
	 * @return 是否忽略
	 */
	boolean ignoreEmptyRow() default false;

	/**
	 * 标题所在行数
	 *
	 * @return 行数
	 */
	int headRowNumber() default 1;

	/**
	 * 自定义处理器
	 *
	 * @return 自定义处理器
	 */
	Class<? extends ExcelReadListener<?>> readEventListener();
}
