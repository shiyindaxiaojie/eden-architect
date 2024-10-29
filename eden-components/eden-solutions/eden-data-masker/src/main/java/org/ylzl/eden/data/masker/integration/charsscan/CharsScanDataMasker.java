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

package org.ylzl.eden.data.masker.integration.charsscan;

import com.github.houbb.chars.scan.bs.CharsScanBs;
import lombok.RequiredArgsConstructor;
import org.ylzl.eden.data.masker.DataMasker;

/**
 * CharsScan 组件
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 * @link https://github.com/houbb
 */
@RequiredArgsConstructor
public class CharsScanDataMasker implements DataMasker {

	private final CharsScanBs charsScanBs;

	@Override
	public String masking(String text) {
		try {
			return charsScanBs.scanAndReplace(text);
		} catch (Exception e) {
			return text;
		}
	}
}
