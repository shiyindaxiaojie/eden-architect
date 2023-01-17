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

package org.ylzl.eden.data.auditor

import org.ylzl.eden.data.auditor.support.DataDifferHelper
import spock.lang.Specification

class DataDifferTest extends Specification {

	TestCase oldVersion = TestCase.builder()
		.chineseName("梦想歌")
		.idCard("440101199103020011")
		.mobilePhone("13524678900")
		.address("广州市天河区梅赛德斯奔驰911室")
		.build()

	TestCase newVersion = TestCase.builder()
		.chineseName("梦想歌")
		.idCard("440101199103020011")
		.mobilePhone("18812345678")
		.address("广州市天河区珠江新城233区")
		.build()

	def "test data differing"() {
		given:
		DataDiffer dataDiffer = DataDifferHelper.dataDiffer(spi)
		TestCase changeVersion = dataDiffer.compare(oldVersion, newVersion).getChanges().get(0).getAffectedValue();

		expect:
		diff == changeVersion.getAddress()

		where:
		spi    		|| diff
		"javers"   	|| "广州市天河区珠江新城233区"
	}
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme
