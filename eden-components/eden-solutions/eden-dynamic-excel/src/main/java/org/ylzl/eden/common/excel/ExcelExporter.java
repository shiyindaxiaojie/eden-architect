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

import com.alibaba.excel.write.handler.WriteHandler;
import org.ylzl.eden.common.excel.writer.sheet.Sheet;
import org.ylzl.eden.commons.lang.Strings;

import java.lang.annotation.*;

/**
 * Excel 导出
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface ExcelExporter {

	/**
	 * 文件名称
	 *
	 * @return 文件名称
	 */
	String name() default Strings.EMPTY;

	/**
	 * 文件格式
	 *
	 * @return 文件格式
	 */
	ExcelType format() default ExcelType.XLSX;

	/**
	 * 文件密码
	 *
	 * @return 文件密码
	 */
	String password() default Strings.EMPTY;

	/**
	 * 工作表
	 *
	 * @return 工作表
	 */
	Sheet[] sheets() default @Sheet(sheetName = "sheet1");

	/**
	 * 包含字段
	 *
	 * @return 字段
	 */
	String[] includes() default {};

	/**
	 * 排除字段
	 *
	 * @return 字段
	 */
	String[] excludes() default {};

	/**
	 * 内存操作
	 *
	 * @return 内存操作
	 */
	boolean inMemory() default false;

	/**
	 * 模板
	 *
	 * @return 模板
	 */
	String template() default Strings.EMPTY;

	/**
	 * 是否开启国际化
	 *
	 * @return 是否开启国际化
	 */
	boolean i18n() default false;

	/**
	 * 是否开启填充模式
	 *
	 * @return 是否开启填充模式
	 */
	boolean fill() default false;

	/**
	 * 自定义处理器
	 *
	 * @return 自定义处理器
	 */
	Class<? extends WriteHandler>[] writeHandler() default {};
}
