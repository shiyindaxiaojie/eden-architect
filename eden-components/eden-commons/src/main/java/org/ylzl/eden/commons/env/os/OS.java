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
package org.ylzl.eden.commons.env.os;

import lombok.Getter;
import lombok.NonNull;
import org.ylzl.eden.commons.regex.RegexUtils;

/**
 * 操作系统
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Getter
public enum OS {

	LINUX("Linux", "linux", ""),
	WINDOWS_10_2016("Windows 10 or Windows Server 2016", "windows nt 10\\.0", "windows nt (10\\.0)"),
	WINDOWS_81_2012R2("Windows 8.1 or Windows Server 2012R2", "windows nt 6\\.3", "windows nt (6\\.3)"),
	WINDOWS_8_2012("Windows 8 or Windows Server 2012", "windows nt 6\\.2", "windows nt (6\\.2)"),
	WINDOWS_VISTA("Windows Vista", "windows nt 6\\.0", "windows nt (6\\.0)"),
	WINDOWS_7_2008R2("Windows 7 or Windows Server 2008R2", "windows nt 6\\.1", "windows nt (6\\.1)"),
	WINDOWS_2003("Windows 2003", "windows nt 5\\.2", "windows nt (5\\.2)"),
	WINDOWS_XP("Windows XP", "windows nt 5\\.1", "windows nt (5\\.1)"),
	WINDOWS_2000("Windows 2000", "windows nt 5\\.0", "windows nt (5\\.0)"),
	WINDOWS_PHONE("Windows Phone", "windows (ce|phone|mobile)( os)?", "windows (?:ce|phone|mobile) (\\d+([._]\\d+)*)"),
	WINDOWS("Windows", "windows", ""),
	DARWIN("Darwin", "Darwin\\/([\\d\\w\\.\\-]+)", "Darwin\\/([\\d\\w\\.\\-]+)"),
	OSX("OSX", "os x (\\d+)[._](\\d+)", "os x (\\d+([._]\\d+)*)"),
	ANDROID("Android", "Android", "Android (\\d+([._]\\d+)*)"),
	I_PAD("iPad", "\\(iPad.*os (\\d+)[._](\\d+)", "\\(iPad.*os (\\d+([._]\\d+)*)"),
	I_PHONE("iPhone", "\\(iPhone.*os (\\d+)[._](\\d+)", "\\(iPhone.*os (\\d+([._]\\d+)*)"),
	Y_POD("YPod", "iPod touch[\\s\\;]+iPhone.*os (\\d+)[._](\\d+)", "iPod touch[\\s\\;]+iPhone.*os (\\d+([._]\\d+)*)"),
	Y_PAD("YPad", "iPad[\\s\\;]+iPhone.*os (\\d+)[._](\\d+)", "iPad[\\s\\;]+iPhone.*os (\\d+([._]\\d+)*)"),
	Y_PHONE("YPhone", "iPhone[\\s\\;]+iPhone.*os (\\d+)[._](\\d+)", "iPhone[\\s\\;]+iPhone.*os (\\d+([._]\\d+)*)"),
	SYMBIAN("Symbian", "symbian(os)?", ""),
	ADOBE_AIR("Adobe Air", "AdobeAir\\/([\\d\\w\\.\\-]+)", "AdobeAir\\/([\\d\\w\\.\\-]+)"),
	WII("Wii", "wii", "wii libnup/(\\d+([._]\\d+)*)"),
	PS3("PS3", "playstation 3", "playstation 3; (\\d+([._]\\d+)*)"),
	PSP("PSP", "playstation portable", "Portable\\); (\\d+([._]\\d+)*)"),
	JAVA("Java", "Java[\\s]+([\\d\\w\\.\\-]+)", "Java[\\s]+([\\d\\w\\.\\-]+)");

	private final String name;
	private final String regex;
	private final String versionRegex;

	OS(String name, String regex, String versionRegex) {
		this.name = name;
		this.regex = regex;
		this.versionRegex = versionRegex;
	}

	public static OS parse(@NonNull String info) {
		for (OS os : OS.values()) {
			if (RegexUtils.find(os.getName(), info)) {
				return os;
			}
		}
		return null;
	}

	public String getVersion(String info) {
		return RegexUtils.group(this.getVersionRegex(), info, 1);
	}
}
