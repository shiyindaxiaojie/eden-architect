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

package org.ylzl.eden.commons.env.platform;

import lombok.NonNull;
import org.ylzl.eden.commons.regex.RegexUtils;

/**
 * 运行平台
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public enum Platform {

	IPHONE("iPhone", "iphone"),
	IPOD("iPod", "ipod"),
	IPAD("iPad", "ipad"),
	ANDROID("Android", "android"),
	GOOGLE_TV("GoogleTV", "googletv"),
	WINDOWS_PHONE("Windows Phone", "windows (ce|phone|mobile)( os)?"),
	HTC_FLYER("htcFlyer", "htc_flyer"),
	SYMBIAN("Symbian", "symbian(os)?"),
	BLACKBERRY("Blackberry", "blackberry"),
	WINDOWS("Windows", "windows"),
	MAC("Mac", "(macintosh|darwin)"),
	LINUX("Linux", "linux"),
	WII("Wii", "wii"),
	PLAYSTATION("Playstation", "playstation"),
	JAVA("Java", "java");

	private final String name;
	private final String regex;

	Platform(String name, String regex) {
		this.name = name;
		this.regex = regex;
	}

	public String getName() {
		return name;
	}

	public String getRegex() {
		return regex;
	}

	public static Platform parse(@NonNull String info) {
		for (Platform platform : Platform.values()) {
			if (RegexUtils.find(platform.getRegex(), info)) {
				return platform;
			}
		}
		return null;
	}
}
