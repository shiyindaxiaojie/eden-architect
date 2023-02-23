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

import com.alibaba.excel.exception.ExcelAnalysisException;
import com.alibaba.excel.exception.ExcelCommonException;
import lombok.Getter;
import org.apache.poi.util.IOUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

/**
 * Excel类型
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Getter
public enum ExcelType {

	CSV(".csv", new byte[]{-27, -89, -109, -27}),
	XLS(".xls", new byte[]{-48, -49, 17, -32, -95, -79, 26, -31}),
	XLSX(".xlsx", new byte[]{80, 75, 3, 4});

	final String value;
	final byte[] magic;
	static final int MAX_PATTERN_LENGTH = 8;

	ExcelType(String value, byte[] magic) {
		this.value = value;
		this.magic = magic;
	}

	public static ExcelType valueOf(File file) {
		try {
			if (!file.exists()) {
				throw new ExcelAnalysisException("File " + file.getAbsolutePath() + " not exists.");
			}
			String fileName = file.getName();
			if (fileName.endsWith(XLSX.getValue())) {
				return XLSX;
			}
			if (fileName.endsWith(XLS.getValue())) {
				return XLS;
			}
			if (fileName.endsWith(CSV.getValue())) {
				return CSV;
			}
			try (BufferedInputStream bufferedInputStream = new BufferedInputStream(Files.newInputStream(file.toPath()))) {
				return recognitionExcelType(bufferedInputStream);
			}
		} catch (ExcelCommonException e) {
			throw e;
		} catch (Exception e) {
			throw new ExcelCommonException(
				"Convert excel format exception.You can try specifying the 'excelType' yourself", e);
		}
	}

	private static ExcelType recognitionExcelType(InputStream inputStream) throws IOException {
		byte[] data = IOUtils.peekFirstNBytes(inputStream, MAX_PATTERN_LENGTH);
		if (findMagic(XLSX.magic, data)) {
			return XLSX;
		}
		if (findMagic(CSV.magic, data)) {
			return CSV;
		}
		if (findMagic(XLS.magic, data)) {
			return XLS;
		}
		throw new IOException("Parse excel type error. You can try specifying the 'ExcelType' yourself");
	}

	private static boolean findMagic(byte[] expected, byte[] actual) {
		int i = 0;
		for (byte expectedByte : expected) {
			if (actual[i++] != expectedByte && expectedByte != '?') {
				return false;
			}
		}
		return true;
	}
}
