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

package org.ylzl.eden.data.filter

import com.google.common.collect.Sets
import org.ylzl.eden.data.filter.sensitive.SensitiveWord
import org.ylzl.eden.data.filter.sensitive.SensitiveWordLoader
import org.ylzl.eden.data.filter.support.DataSensitiveFilterHelper
import spock.lang.Specification

class DataSensitiveFilterTest extends Specification {

	static String sensitiveText = "我是大鸡巴美少女，喜欢玩美少女万华镜、3D定制女仆"

	DataSensitiveFilter dataSensitiveFilter

	def setup() {
		dataSensitiveFilter = DataSensitiveFilterHelper.dataSensitiveFilter(new SensitiveWordLoader() {

			@Override
			Collection<String> loadSensitiveWords() {
				return Sets.newHashSet("鸡巴", "女仆")
			}
		})
	}

	def "test parse text"() {
		given:
		Collection<SensitiveWord> sensitiveWords = dataSensitiveFilter.parseText(sensitiveText)

		expect:
		sensitiveWords[index].getKeyword() == keyword
		for (SensitiveWord sensitiveWord : sensitiveWords) {
			sensitiveWord.getKeyword() == keyword
		}

		where:
		index	|| 	keyword
		0		||	"鸡巴"
		1		||	"女仆"
	}

	def "test filter text"() {
		expect:
		replacedText == dataSensitiveFilter.replaceSensitiveWords(sensitiveText)
		deletedText	== dataSensitiveFilter.deleteSensitiveWords(sensitiveText)

		where:
		replacedText 								|| deletedText
		"我是大???美少女，喜欢玩美少女万华镜、3D定制???"	|| "我是大美少女，喜欢玩美少女万华镜、3D定制"
	}
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme
