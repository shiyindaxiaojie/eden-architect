package org.ylzl.eden.commons.regex.pattern;

import lombok.experimental.UtilityClass;

import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * 正则表达式缓存
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@UtilityClass
public class RegexCache {

	/**
	 * 字母
	 */
	public static final Pattern WORD = Pattern.compile(Regex.WORD);

	/**
	 * 汉字
	 */
	public static final Pattern CHINESE = Pattern.compile(Regex.CHINESE);

	/**
	 * 用户名：取值范围为 a-z、A-Z、0-9、"_"、汉字，不能以"_"结尾，长度在 1~18 区间
	 */
	public static final Pattern USERNAME = Pattern.compile(Regex.USERNAME);

	/**
	 * 邮箱，符合RFC 5322规范
	 */
	public static final Pattern EMAIL = Pattern.compile(Regex.EMAIL);

	/**
	 * 移动电话
	 */
	public static final Pattern MOBILE = Pattern.compile(Regex.MOBILE);

	/**
	 * 中国香港移动电话
	 * <br/>中国大陆：+86 Mainland China
	 * <br/>中国香港：+852 Hong Kong
	 * <br/>中国澳门：+853 Macao
	 * <br/>中国台湾：+886 Taiwan
	 */
	public static final Pattern MOBILE_HK = Pattern.compile(Regex.MOBILE_HK);

	/**
	 * 中国台湾移动电话
	 * <br/>中国台湾：+886 Taiwan
	 */
	public static final Pattern MOBILE_TW = Pattern.compile(Regex.MOBILE_TW);

	/**
	 * 中国澳门移动电话
	 * <br/>中国澳门 +853 Macao
	 */
	public static final Pattern MOBILE_MO = Pattern.compile(Regex.MOBILE_MO);

	/**
	 * 座机号码
	 */
	public static final Pattern TELEPHONE = Pattern.compile(Regex.TELEPHONE);

	/**
	 * 座机号码 +400/+800
	 */
	public static final Pattern TELEPHONE_400_800 = Pattern.compile(Regex.TELEPHONE_400_800);

	/**
	 * 身份证号码（15 位）
	 */
	public static final Pattern ID_CARD_15 = Pattern.compile(Regex.ID_CARD_15);

	/**
	 * 身份证号码（18 位）
	 */
	public static final Pattern ID_CARD_18 = Pattern.compile(Regex.ID_CARD_18);

	/**
	 * 社会统一信用代码
	 */
	public static final Pattern CREDIT_CODE = Pattern.compile(Regex.CREDIT_CODE);

	/**
	 * 邮编号码
	 */
	public static final Pattern ZIP_CODE = Pattern.compile(Regex.ZIP_CODE);

	/**
	 * 生日
	 */
	public static final Pattern BIRTHDAY = Pattern.compile(Regex.BIRTHDAY);

	/**
	 * QQ号码
	 */
	public static final Pattern TENCENT_QQ = Pattern.compile(Regex.TENCENT_QQ);

	/**
	 * 车牌号码（中国）
	 */
	public static final Pattern PLATE_NUMBER_CN = Pattern.compile(Regex.PLATE_NUMBER_CN);

	/**
	 * 车架号
	 */
	public static final Pattern CAR_VIN = Pattern.compile(Regex.CAR_VIN);

	/**
	 * 驾驶证
	 */
	public static final Pattern CAR_DRIVING_LICENCE = Pattern.compile(Regex.CAR_DRIVING_LICENCE);

	/**
	 * IPv4
	 */
	public static final Pattern IPV4 = Pattern.compile(Regex.IPV4);

	/**
	 * IPv6
	 */
	public static final Pattern IPV6 = Pattern.compile(Regex.IPV6);

	/**
	 * URL
	 */
	public static final Pattern URL = Pattern.compile(Regex.URL);

	/**
	 * HTTP
	 */
	public static final Pattern HTTP = Pattern.compile(Regex.HTTP);

	/**
	 * 货币
	 */
	public static final Pattern MONEY = Pattern.compile(Regex.MONEY);

	/**
	 * 日期
	 */
	public static final Pattern DATE = Pattern.compile(Regex.DATE);

	/**
	 * 时间
	 */
	public static final Pattern TIME = Pattern.compile(Regex.TIME);

	/**
	 * UUID
	 */
	public static final Pattern UUID = Pattern.compile(Regex.UUID);

	/**
	 * MAC
	 */
	public static final Pattern MAC_ADDRESS = Pattern.compile(Regex.MAC_ADDRESS);

	/**
	 * 十六进制字符串
	 */
	public static final Pattern HEX = Pattern.compile(Regex.HEX);

	/**
	 * 整数
	 */
	public static final Pattern INTEGER = Pattern.compile(Regex.INTEGER);

	/**
	 * 正整数
	 */
	public static final Pattern INTEGER_POSITIVE = Pattern.compile(Regex.INTEGER_POSITIVE);

	/**
	 * 非正整数
	 */
	public static final Pattern INTEGER_POSITIVE_REVERSE = Pattern.compile(Regex.INTEGER_POSITIVE_REVERSE);

	/**
	 * 负整数
	 */
	public static final Pattern INTEGER_NEGATIVE = Pattern.compile(Regex.INTEGER_NEGATIVE);

	/**
	 * 非负整数
	 */
	public static final Pattern INTEGER_NEGATIVE_REVERSE = Pattern.compile(Regex.INTEGER_NEGATIVE_REVERSE);

	/**
	 * 浮点数
	 */
	public static final Pattern FLOAT = Pattern.compile(Regex.FLOAT);

	/**
	 * 正浮点数
	 */
	public static final Pattern FLOAT_POSITIVE = Pattern.compile(Regex.FLOAT_POSITIVE);

	/**
	 * 负浮点数
	 */
	public static final Pattern FLOAT_NEGATIVE = Pattern.compile(Regex.FLOAT_NEGATIVE);

	/**
	 * 正则编译缓存
	 */
	private static final ConcurrentHashMap<String, Pattern> CACHE = new ConcurrentHashMap<>();

	/**
	 * 获取缓存，如果获取不到，先编译后存入缓存
	 *
	 * @param regex 正则字符串
	 * @param flags 匹配规则
	 * @return 正则表达式
	 */
	public static Pattern get(String regex, int flags) {
		Pattern pattern = CACHE.get(regex);
		if (pattern == null) {
			pattern = Pattern.compile(regex, flags);
			CACHE.put(regex, pattern);
		}
		return pattern;
	}

	/**
	 * 移除缓存
	 *
	 * @param regex 正则字符串
	 * @return 正则表达式
	 */
	public static Pattern remove(String regex) {
		return CACHE.remove(regex);
	}

	/**
	 * 清空缓存
	 */
	public static void clear() {
		CACHE.clear();
	}
}
