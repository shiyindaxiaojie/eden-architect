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
package org.ylzl.eden.commons.env.browser;

import com.google.common.net.HttpHeaders;
import lombok.Getter;
import lombok.NonNull;
import org.ylzl.eden.commons.regex.RegexUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;

/**
 * 浏览器枚举
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Getter
public enum Browser {

	WX_WORK("wxwork", "wxwork", compile("wxwork\\/([\\d\\w\\.\\-]+)")), // 企业微信
	MICRO_MESSENGER("MicroMessenger", "MicroMessenger", compile("MicroMessenger" + Browser.DEFAULT_VERSION)), // 微信
	MINI_PROGRAM("miniProgram", "miniProgram", compile("miniProgram" + Browser.DEFAULT_VERSION)), // 微信小程序
	QQ_BROWSER("QQBrowser", "MQQBrowser", compile("MQQBrowser\\/([\\d\\w\\.\\-]+)")), // QQ浏览器
	DING_TALK_WIN("DingTalk-win", "dingtalk-win", compile("DingTalk\\(([\\d\\w\\.\\-]+)\\)")), // 钉钉PC端浏览器
	DING_TALK("DingTalk", "DingTalk", compile("AliApp\\(DingTalk\\/([\\d\\w\\.\\-]+)\\)")), // 钉钉内置浏览器
	ALIPAY("Alipay", "AlipayClient", compile("AliApp\\(AP\\/([\\d\\w\\.\\-]+)\\)")), // 支付宝内置浏览器
	TAOBAO("Taobao", "taobao", compile("AliApp\\(TB\\/([\\d\\w\\.\\-]+)\\)")), // 淘宝内置浏览器
	UC_BROWSER("UCBrowser", "UC?Browser", compile("UC?Browser\\/([\\d\\w\\.\\-]+)")), // UC浏览器
	MIUI_BROWSER("MiuiBrowser", "MiuiBrowser|mibrowser", compile("MiuiBrowser\\/([\\d\\w\\.\\-]+)")), // 小米浏览器
	QUARK("Quark", "Quark", compile("Quark" + Browser.DEFAULT_VERSION)), // 夸克浏览器
	LENOVO("Lenovo", "SLBrowser", compile("SLBrowser/([\\d\\w\\.\\-]+)")), // 联想浏览器
	MS_EDGE("MSEdge", "Edge|Edg", compile("(?:edge|Edg|EdgA)\\/([\\d\\w\\.\\-]+)")),
	CHROME("Chrome", "chrome", compile("Chrome" + Browser.DEFAULT_VERSION)),
	FIREFOX("Firefox", "firefox", compile("Firefox" + Browser.DEFAULT_VERSION)),
	IE_MOBILE("IEMobile", "iemobile", compile("IEMobile" + Browser.DEFAULT_VERSION)),
	ANDROID_BROWSER("Android Browser", "android", compile("version\\/([\\d\\w\\.\\-]+)")),
	Safari("Safari", "safari", compile("version\\/([\\d\\w\\.\\-]+)")),
	Opera("Opera", "opera", compile("Opera" + Browser.DEFAULT_VERSION)),
	KONQUEROR("Konqueror", "konqueror", compile("Konqueror" + Browser.DEFAULT_VERSION)),
	PS3("PS3", "playstation 3", compile("([\\d\\w\\.\\-]+)\\)\\s*$")),
	PSP("PSP", "playstation portable", compile("([\\d\\w\\.\\-]+)\\)?\\s*$")),
	LOTUS("Lotus", "lotus.notes", compile("Lotus-Notes\\/([\\w.]+)")),
	THUNDERBIRD("Thunderbird", "thunderbird", compile("Thunderbird" + Browser.DEFAULT_VERSION)),
	NETSCAPE("Netscape", "netscape", compile("Netscape" + Browser.DEFAULT_VERSION)),
	SEAMONKEY("Seamonkey", "seamonkey", compile("Seamonkey" + Browser.DEFAULT_VERSION)),
	OUTLOOK("Outlook", "microsoft.outlook", compile("Outlook" + Browser.DEFAULT_VERSION)),
	EVOLUTION("Evolution", "evolution", compile("Evolution" + Browser.DEFAULT_VERSION)),
	MSIE("MSIE", "msie", compile("msie ([\\d\\w\\.\\-]+)")),
	MSIE11("MSIE11", "rv:11", compile("rv:([\\d\\w\\.\\-]+)")),
	GABBLE("Gabble", "Gabble", compile("Gabble" + Browser.DEFAULT_VERSION)),
	YAMMER_DESKTOP("Yammer Desktop", "AdobeAir", compile("([\\d\\w\\.\\-]+)\\/Yammer")),
	YAMMER_MOBILE("Yammer Mobile", "Yammer[\\s]+([\\d\\w\\.\\-]+)",  compile("Yammer[\\s]+([\\d\\w\\.\\-]+)")),
	APACHE_HTTP_CLIENT("Apache HTTP Client", "Apache\\\\-HttpClient", compile("Apache\\-HttpClient\\/([\\d\\w\\.\\-]+)")),
	BLACKBERRY("BlackBerry", "BlackBerry", compile("BlackBerry[\\d]+\\/([\\d\\w\\.\\-]+)"));

	public static final String DEFAULT_VERSION = "[\\/ ]([\\d\\w\\.\\-]+)";

	private final String name;
	private final String regex;
	private final Pattern pattern;

	Browser(String name, String regex, Pattern pattern) {
		this.name = name;
		this.regex = regex;
		this.pattern = pattern;
	}

	public static Browser parse(@NonNull HttpServletRequest request) {
		final String userAgent = request.getHeader(HttpHeaders.USER_AGENT);
		for (Browser browser : Browser.values()) {
			if (RegexUtils.find(browser.getRegex(), userAgent)) {
				return browser;
			}
		}
		return null;
	}

	private static Pattern compile(String regex) {
		return Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
	}

	public static void main(String[] args) {
		for (Browser browser : Browser.values()) {
			System.out.println(RegexUtils.group(browser.getPattern().pattern(), "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36"));
		}

	}
}
