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

import cn.hutool.http.useragent.UserAgentParser;
import com.google.common.net.HttpHeaders;
import lombok.Getter;
import lombok.NonNull;
import org.ylzl.eden.commons.regex.RegexUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 浏览器枚举
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Getter
public enum Browser {

	WX_WORK("wxwork", "wxwork", "wxwork\\/([\\d\\w\\.\\-]+)"), // 企业微信
	MICRO_MESSENGER("MicroMessenger", "MicroMessenger", "MicroMessenger" + Browser.DEFAULT_VERSION), // 微信
	MINI_PROGRAM("miniProgram", "miniProgram", "miniProgram" + Browser.DEFAULT_VERSION), // 微信小程序
	QQ_BROWSER("QQBrowser", "MQQBrowser", "MQQBrowser\\/([\\d\\w\\.\\-]+)"), // QQ浏览器
	DING_TALK_WIN("DingTalk-win", "dingtalk-win", "DingTalk\\(([\\d\\w\\.\\-]+)\\)"), // 钉钉PC端浏览器
	DING_TALK("DingTalk", "DingTalk", "AliApp\\(DingTalk\\/([\\d\\w\\.\\-]+)\\)"), // 钉钉内置浏览器
	ALIPAY("Alipay", "AlipayClient", "AliApp\\(AP\\/([\\d\\w\\.\\-]+)\\)"), // 支付宝内置浏览器
	TAOBAO("Taobao", "taobao", "AliApp\\(TB\\/([\\d\\w\\.\\-]+)\\)"), // 淘宝内置浏览器
	UC_BROWSER("UCBrowser", "UC?Browser", "UC?Browser\\/([\\d\\w\\.\\-]+)"), // UC浏览器
	MIUI_BROWSER("MiuiBrowser", "MiuiBrowser|mibrowser", "MiuiBrowser\\/([\\d\\w\\.\\-]+)"), // 小米浏览器
	QUARK("Quark", "Quark", "Quark" + Browser.DEFAULT_VERSION), // 夸克浏览器
	LENOVO("Lenovo", "SLBrowser", "SLBrowser/([\\d\\w\\.\\-]+)"), // 联想浏览器
	MS_EDGE("MSEdge", "Edge|Edg", "(?:edge|Edg|EdgA)\\/([\\d\\w\\.\\-]+)"),
	CHROME("Chrome", "chrome", "Chrome" + Browser.DEFAULT_VERSION),
	FIREFOX("Firefox", "firefox", "Firefox" + Browser.DEFAULT_VERSION),
	IE_MOBILE("IEMobile", "iemobile", "IEMobile" + Browser.DEFAULT_VERSION),
	ANDROID_BROWSER("Android Browser", "android", "version\\/([\\d\\w\\.\\-]+)"),
	Safari("Safari", "safari", "version\\/([\\d\\w\\.\\-]+)"),
	Opera("Opera", "opera", "Opera" + Browser.DEFAULT_VERSION),
	KONQUEROR("Konqueror", "konqueror", "Konqueror" + Browser.DEFAULT_VERSION),
	PS3("PS3", "playstation 3", "([\\d\\w\\.\\-]+)\\)\\s*$"),
	PSP("PSP", "playstation portable", "([\\d\\w\\.\\-]+)\\)?\\s*$"),
	LOTUS("Lotus", "lotus.notes", "Lotus-Notes\\/([\\w.]+)"),
	THUNDERBIRD("Thunderbird", "thunderbird", "Thunderbird" + Browser.DEFAULT_VERSION),
	NETSCAPE("Netscape", "netscape", "Netscape" + Browser.DEFAULT_VERSION),
	SEAMONKEY("Seamonkey", "seamonkey", "Seamonkey" + Browser.DEFAULT_VERSION),
	OUTLOOK("Outlook", "microsoft.outlook", "Outlook" + Browser.DEFAULT_VERSION),
	EVOLUTION("Evolution", "evolution", "Evolution" + Browser.DEFAULT_VERSION),
	MSIE("MSIE", "msie", "msie ([\\d\\w\\.\\-]+)"),
	MSIE11("MSIE11", "rv:11", "rv:([\\d\\w\\.\\-]+)"),
	GABBLE("Gabble", "Gabble", "Gabble" + Browser.DEFAULT_VERSION),
	YAMMER_DESKTOP("Yammer Desktop", "AdobeAir", "([\\d\\w\\.\\-]+)\\/Yammer"),
	YAMMER_MOBILE("Yammer Mobile", "Yammer[\\s]+([\\d\\w\\.\\-]+)",  "Yammer[\\s]+([\\d\\w\\.\\-]+)"),
	APACHE_HTTP_CLIENT("Apache HTTP Client", "Apache\\\\-HttpClient", "Apache\\-HttpClient\\/([\\d\\w\\.\\-]+)"),
	BLACKBERRY("BlackBerry", "BlackBerry", "BlackBerry[\\d]+\\/([\\d\\w\\.\\-]+)");

	public static final String DEFAULT_VERSION = "[\\/ ]([\\d\\w\\.\\-]+)";

	private final String name;
	private final String regex;
	private final String versionRegex;

	Browser(String name, String regex, String versionRegex) {
		this.name = name;
		this.regex = regex;
		this.versionRegex = versionRegex;
	}

	public String getName() {
		return name;
	}

	public String getRegex() {
		return regex;
	}

	public String getVersionRegex() {
		return versionRegex;
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

	public String getVersion(String userAgent) {
		return RegexUtils.group(this.getVersionRegex(), userAgent, 1);
	}

	public static void main(String[] args) {
		String input = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) " +
			"Chrome/111.0.0.0 Safari/537.36";
		System.out.println(RegexUtils.group(Browser.CHROME.getVersionRegex(), input, 0));

		System.out.println(UserAgentParser.parse(input).getBrowser().getVersion(input));
	}
}
