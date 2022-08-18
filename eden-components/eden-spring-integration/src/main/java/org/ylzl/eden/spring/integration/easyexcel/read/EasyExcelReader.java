/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ylzl.eden.spring.integration.easyexcel.read;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.metadata.ReadSheet;
import lombok.NonNull;
import org.ylzl.eden.commons.lang.reflect.ReflectionUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * EasyExcel 读取器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class EasyExcelReader {

	public <T> ExcelReader excelReader(
		@NonNull InputStream inputStream, @NonNull AnalysisEventListener<T> listener) {
		Class<T> head = ReflectionUtils.getActualTypeArgument(listener.getClass());
		return EasyExcel.read(inputStream, head, listener).build();
	}

	public <T> void read(
		@NonNull InputStream inputStream,
		@NonNull AnalysisEventListener<T> listener,
		int... sheetIndexs) {
		ExcelReader excelReader = excelReader(inputStream, listener);

		try {
			if (sheetIndexs == null) {
				excelReader.readAll();
			} else {
				List<ReadSheet> readSheets = new ArrayList<>();
				for (int sheetIndex : sheetIndexs) {
					readSheets.add(EasyExcel.readSheet(sheetIndex).build());
				}
				excelReader.read(readSheets);
			}
		} finally {
			excelReader.finish();
		}
	}
}
