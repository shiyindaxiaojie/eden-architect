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
package org.ylzl.eden.commons.regex.pattern;

import lombok.experimental.UtilityClass;

/**
 * 正则表达式常量定义
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@UtilityClass
public class Regex {

	/**
	 * 字母
	 */
	public static final String WORD = "[a-zA-Z]+";

	/**
	 * 汉字
	 */
	public static final String CHINESE = "^[\\u4e00-\\u9fa5]+$";

	/**
	 * 用户名：取值范围为 a-z、A-Z、0-9、"_"、汉字，不能以"_"结尾，长度在 1~18 区间
	 */
	public static final String USERNAME = "^[\\w\\u4e00-\\u9fa5]{1,18}(?<!_)$";

	/**
	 * 邮箱，符合RFC 5322规范
	 */
	public static final String EMAIL = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])";

	/**
	 * 移动电话
	 */
	public static final String MOBILE = "(?:0|86|\\+86)?1[3-9]\\d{9}";

	/**
	 * 中国香港移动电话
	 * <br/>中国大陆：+86 Mainland China
	 * <br/>中国香港：+852 Hong Kong
	 * <br/>中国澳门：+853 Macao
	 * <br/>中国台湾：+886 Taiwan
	 */
	public static final String MOBILE_HK = "(?:0|852|\\+852)?\\d{8}";

	/**
	 * 中国台湾移动电话
	 * <br/>中国台湾：+886 Taiwan
	 */
	public static final String MOBILE_TW = "(?:0|886|\\+886)?(?:|-)09\\d{8}";

	/**
	 * 中国澳门移动电话
	 * <br/>中国澳门 +853 Macao
	 */
	public static final String MOBILE_MO = "(?:0|853|\\+853)?(?:|-)6\\d{7}";

	/**
	 * 座机号码
	 */
	public static final String TELEPHONE = "(010|02\\d|0[3-9]\\d{2})-?(\\d{6,8})";

	/**
	 * 座机号码 +400/+800
	 */
	public static final String TELEPHONE_400_800 = "0\\d{2,3}[\\- ]?[1-9]\\d{6,7}|[48]00[\\- ]?[1-9]\\d{6}";

	/**
	 * 身份证号码（15 位）
	 */
	public static final String ID_CARD_15 = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$";

	/**
	 * 身份证号码（18 位）
	 */
	public static final String ID_CARD_18 = "[1-9]\\d{5}[1-2]\\d{3}((0\\d)|(1[0-2]))(([012]\\d)|3[0-1])\\d{3}(\\d|X|x)";

	/**
	 * 社会统一信用代码
	 */
	public static final String CREDIT_CODE = "^[0-9A-HJ-NPQRTUWXY]{2}\\d{6}[0-9A-HJ-NPQRTUWXY]{10}$";

	/**
	 * 邮编号码
	 */
	public static final String ZIP_CODE = "^(0[1-7]|1[0-356]|2[0-7]|3[0-6]|4[0-7]|5[0-7]|6[0-7]|7[0-5]|8[0-9]|9[0-8])\\d{4}|99907[78]$";

	/**
	 * 生日
	 */
	public static final String BIRTHDAY = "^(\\d{2,4})([/\\-.年]?)(\\d{1,2})([/\\-.月]?)(\\d{1,2})日?$";

	/**
	 * QQ号码
	 */
	public static final String TENCENT_QQ = "[1-9][0-9]{4,}";

	/**
	 * 车牌号码（中国）
	 */
	public static final String PLATE_NUMBER_CN = "^(([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领]" +
		"[A-Z](([0-9]{5}[ABCDEFGHJK])|([ABCDEFGHJK]([A-HJ-NP-Z0-9])[0-9]{4})))|" +
		"([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领]\\d{3}\\d{1,3}[领])|" +
		"([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-Z][A-HJ-NP-Z0-9]{4}[A-HJ-NP-Z0-9挂学警港澳使领]))$";

	/**
	 * 车架号
	 */
	public static final String CAR_VIN = "^[A-Za-z0-9]{17}$";

	/**
	 * 驾驶证
	 */
	public static final String CAR_DRIVING_LICENCE = "^[0-9]{12}$";

	/**
	 * IPv4
	 */
	public static final String IPV4 = "\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)" +
		"\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)" +
		"\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b";

	/**
	 * IPv6
	 */
	public static final String IPV6 = "(([0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|" +
		"([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|" +
		"([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|" +
		"([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:" +
		"((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]+|::(ffff(:0{1,4})?:)?" +
		"((25[0-5]|(2[0-4]|1?[0-9])?[0-9])\\.){3}(25[0-5]|(2[0-4]|1?[0-9])?[0-9])|([0-9a-fA-F]{1,4}:)" +
		"{1,4}:((25[0-5]|(2[0-4]|1?[0-9])?[0-9])\\.){3}(25[0-5]|(2[0-4]|1?[0-9])?[0-9]))";

	/**
	 * URL
	 */
	public static final String URL = "[a-zA-z]+://[^\\s]*";

	/**
	 * HTTP
	 */
	public static final String HTTP = "(https://|http://)?([\\w-]+\\.)+[\\w-]+(:\\d+)*(/[\\w- ./?%&=]*)?";

	/**
	 * 货币
	 */
	public static final String MONEY = "^(\\d+(?:\\.\\d+)?)$";

	/**
	 * 日期
	 */
	public static final String DATE = "^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|"
		+ "(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|"
		+ "(?:0[48]|[2468][048]|[13579][26])00)-02-29)$";

	/**
	 * 时间
	 */
	public static final String TIME = "\\d{1,2}:\\d{1,2}(:\\d{1,2})?";

	/**
	 * UUID
	 */
	public static final String UUID = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$";

	/**
	 * MAC
	 */
	public static final String MAC_ADDRESS = "((?:[a-fA-F0-9]{1,2}[:-]){5}[a-fA-F0-9]{1,2})|0x(\\d{12}).+ETHER";

	/**
	 * 十六进制字符串
	 */
	public static final String HEX = "^[a-fA-F0-9]+$";

	/**
	 * 整数
	 */
	public static final String INTEGER = "^-?[1-9]\\d*$";

	/**
	 * 正整数
	 */
	public static final String INTEGER_POSITIVE = "^[1-9]\\d*$";

	/**
	 * 非正整数
	 */
	public static final String INTEGER_POSITIVE_REVERSE = "^-[1-9]\\d*|0$";

	/**
	 * 负整数
	 */
	public static final String INTEGER_NEGATIVE = "^-[1-9]\\d*$";

	/**
	 * 非负整数
	 */
	public static final String INTEGER_NEGATIVE_REVERSE = "^[1-9]\\d*|0$";

	/**
	 * 浮点数
	 */
	public static final String FLOAT = "^-?[1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*$";

	/**
	 * 正浮点数
	 */
	public static final String FLOAT_POSITIVE = "^[1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*$";

	/**
	 * 负浮点数
	 */
	public static final String FLOAT_NEGATIVE = "^-[1-9]\\d*\\.\\d*|-0\\.\\d*[1-9]\\d*$";
}
