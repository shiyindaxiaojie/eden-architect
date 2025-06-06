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

package org.ylzl.eden.data.masker.spi;

import org.ylzl.eden.data.masker.DataMasker;

/**
 * 金额数据脱敏
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class MoneyDataMasker implements DataMasker {

	/**
	 * 脱敏处理
	 *
	 * @param text 原始内容
	 * @return 脱敏内容
	 */
	@Override
	public String masking(String text) {
		return "****";
	}
}
