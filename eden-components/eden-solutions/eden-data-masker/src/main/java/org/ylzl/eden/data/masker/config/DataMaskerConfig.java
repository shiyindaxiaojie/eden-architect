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

package org.ylzl.eden.data.masker.config;

import com.google.common.base.Enums;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.ylzl.eden.data.masker.support.DataMaskerStrategy;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据脱敏配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@EqualsAndHashCode
@ToString
@Setter
@Getter
public class DataMaskerConfig {

	/** 是否允许覆盖扩展点 */
	private boolean allowExtensionOverriding = false;


	public static final String CHARS_SCAN = "chars-scan";

	private String type = CHARS_SCAN;

	private final CharsScan charsScan = new CharsScan();

	@EqualsAndHashCode(callSuper = false)
	@ToString
	@Setter
	@Getter
	public static class CharsScan {

		private String prefix = "：‘“，| ,:\\\"'=";

		private String scanList = "1,2,3,4,9";

		private String replaceList = "1,2,3,4,9";

		private String defaultReplace = "12";

		private String replaceHash = "md5";

		private String whiteList = "";

		public String getScanList() {
			return convertToValues(scanList);
		}

		public String getReplaceList() {
			return convertToValues(replaceList);
		}

		public String getDefaultReplace() {
			return convertToValues(defaultReplace);
		}

		private String convertToValues(String input) {
			if (input == null || input.isEmpty()) {
				return input;
			}

			List<String> items = Arrays.asList(input.split(","));
			List<Integer> values = items.stream()
				.map(item -> {
					try {
						return Integer.parseInt(item.trim());
					} catch (NumberFormatException e) {
						DataMaskerStrategy strategy = Enums.getIfPresent(DataMaskerStrategy.class, item.trim()).orNull();
						if (strategy == null) {
							throw new IllegalArgumentException("Invalid masking type: " + item);
						}
						return strategy.getValue();
					}
				})
				.collect(Collectors.toList());

			return values.stream()
				.map(String::valueOf)
				.collect(Collectors.joining(","));
		}
	}
}
