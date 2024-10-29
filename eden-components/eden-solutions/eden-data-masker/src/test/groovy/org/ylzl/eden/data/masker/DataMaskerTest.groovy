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

package org.ylzl.eden.data.masker

import org.ylzl.eden.data.masker.integration.jackson.JacksonDataMaskingModule
import org.ylzl.eden.data.masker.support.DataMaskerHelper
import org.ylzl.eden.spring.framework.json.support.JSONHelper
import spock.lang.Specification

class DataMaskerTest extends Specification {

	TestCase testCase = TestCase.builder()
		.chineseName("梦想歌")
		.idCard("440101199103020011")
		.mobilePhone("13524678900")
		.address("广州市天河区梅赛德斯奔驰911室")
		.build()

	static String testJson = "{\"address\":\"广州市****区梅赛德斯奔驰91****\",\"chineseName\":\"梦*歌\",\"idCard\":\"4401****0011\",\"mobilePhone\":\"135****8900\"}"

	def setup() {
		DataMaskerManager.loadExtensions()
		JacksonDataMaskingModule.register()
	}

	def "test data masking"() {
		given:
		DataMasker dataMasker = DataMaskerHelper.dataMasker(strategy)

		expect:
		maskedData == dataMasker.masking(data)

		where:
		strategy       || data                            || maskedData
		"address"      || "广州市天河区梅赛德斯奔驰911室" || "广州市****区梅赛德斯奔驰91****"
		"bank-card"    || "6227012345678900111"           || "****0111"
		"car-license"  || "粤A88888"                      || "粤A8***8"
		"chinese-name" || "梦想歌"                        || "梦*歌"
		"email"        || "1813986321@qq.com"             || "1****@qq.com"
		"id-card"      || "440101199103020011"            || "4401****0011"
		"mobile-phone" || "13524678900"                   || "135****8900"
		"money"        || "1000000"                       || "****"
		"password"     || "A1S2D3F4G5"                    || "******"
		"telephone"    || "02012345"                      || "020****45"
		"username"     || "mengxiangge"                   || "m****ge"
	}

	def "test json masking"() {
		expect:
		JSONHelper.json(spi).toJSONString(testCase) == maskedJson

		where:
		spi         || maskedJson
		"jackson"   || testJson
		"fastjson"  || testJson
		"fastjson2" || testJson
	}
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme
