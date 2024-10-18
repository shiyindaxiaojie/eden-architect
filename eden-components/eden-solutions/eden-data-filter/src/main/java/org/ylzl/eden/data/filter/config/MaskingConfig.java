package org.ylzl.eden.data.filter.config;

import com.google.common.base.Enums;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.ylzl.eden.data.filter.masking.MaskingStrategy;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据脱敏配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@EqualsAndHashCode
@ToString
@Setter
@Getter
public class MaskingConfig {

	private String type = "chars-scan";

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
						MaskingStrategy strategy = Enums.getIfPresent(MaskingStrategy.class, item.trim()).orNull();
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
