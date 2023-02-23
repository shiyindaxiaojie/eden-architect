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

package org.ylzl.eden.common.excel

import org.ylzl.eden.common.excel.reader.PersistenceExcelReadListener
import org.ylzl.eden.commons.io.ResourceUtils
import org.ylzl.eden.common.excel.support.ExcelReaderHelper
import spock.lang.Specification

class ExcelReaderTest extends Specification {

	def "test read excel"() {
		when:
		try (
			InputStream is = ResourceUtils.getInputStreamFromResource("test/test-case.xlsx")
		) {
			assert is != null
			PersistenceExcelReadListener<TestCase> listener = new PersistenceExcelReadListener<TestCase>() {

				@Override
				void batchSaveData(List<TestCase> data) {
					println("Batch save data: " + data)
				}
			}
			listener.setBatchSize(5)
			ExcelReaderHelper.excelReader().read(is, TestCase.class, listener)
			println("Batch save data validated errors: " + listener.getErrors())
		}

		then:
		notThrown(Exception)
	}

}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme
