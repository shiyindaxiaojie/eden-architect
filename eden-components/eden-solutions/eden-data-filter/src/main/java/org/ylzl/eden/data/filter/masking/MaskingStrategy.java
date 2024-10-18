package org.ylzl.eden.data.filter.masking;

import lombok.Getter;

/**
 * 脱敏策略
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Getter
public enum MaskingStrategy {

	TELEPHONE(1, "手机号"),
	ID_CARD(2, "身份证"),
	BANK_CARD(3, "银行卡"),
	EMAIL(4, "邮箱"),
	CHINESE_NAME(5, "中国人名"),
	BIRTH_DATE(6, "出生日期"),
	GPS(7, "GPS"),
	IPV4(8, "IPV4"),
	ADDRESS(9, "地址"),
	PASSPORT(10, "护照"),
	ANY_NOT_MASKED(11, "匹配任意不掩盖"),
	ANY_PARTIALLY_MASKED(12, "匹配任意半掩盖"),
	ANY_FULLY_MASKED(13, "匹配任意全掩盖");

	private final int value;
	private final String description;

	MaskingStrategy(int value, String description) {
		this.value = value;
		this.description = description;
	}
}
