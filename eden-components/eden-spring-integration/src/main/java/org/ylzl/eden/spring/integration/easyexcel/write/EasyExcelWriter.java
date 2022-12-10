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

package org.ylzl.eden.spring.integration.easyexcel.write;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import lombok.NonNull;
import org.ylzl.eden.commons.lang.reflect.ReflectionUtils;

import java.io.OutputStream;
import java.util.List;
import java.util.Set;

/**
 * EasyExcel 写入器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class EasyExcelWriter {

	public <T> ExcelWriter excelWriter(@NonNull OutputStream outputStream, @NonNull Class<T> head) {
		return EasyExcel.write(outputStream, head).build();
	}

	public <T> void write(
		@NonNull OutputStream outputStream,
		@NonNull List<T> datas,
		@NonNull String sheetName,
		Set<String> includeColumns) {
		Class<T> head = ReflectionUtils.getActualTypeArgument(datas.getClass());
		ExcelWriter excelWriter = excelWriter(outputStream, head);

		WriteSheet writeSheet = EasyExcel.writerSheet(sheetName).build();
		if (includeColumns != null && !includeColumns.isEmpty()) {
			writeSheet.setIncludeColumnFiledNames(includeColumns); // FIXME：写错单词，这个 API 后面肯定会变化
		}
		excelWriter.write(datas, writeSheet);
	}
}
