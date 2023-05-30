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

import org.ylzl.eden.common.excel.support.ExcelHelper
import spock.lang.Specification

class ExcelWriterTest extends Specification {

	List<TestCase> cases = new ArrayList<>()

	def setup() {
		cases.add(TestCase.builder().chineseName("test1").build())
		cases.add(TestCase.builder().chineseName("test2").build())
		cases.add(TestCase.builder().chineseName("test3").build())
	}

	def "test write excel"() {
		when:
		File file = new File("target/test.xlsx")

		ExcelHelper.writer().write(file, cases, TestCase.class)

		then:
		notThrown(Exception)
	}

}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme
