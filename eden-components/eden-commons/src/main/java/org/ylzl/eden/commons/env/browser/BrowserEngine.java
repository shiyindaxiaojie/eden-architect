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

import lombok.NonNull;
import org.ylzl.eden.commons.regex.RegexUtils;

/**
 * 浏览器引擎
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public enum BrowserEngine {

	TRIDENT("Trident", "trident"),
	WEBKIT("Webkit", "webkit"),
	CHROME("Chrome", "chrome"),
	OPERA("Opera", "opera"),
	PRESTO("Presto", "presto"),
	GECKO("Gecko", "gecko"),
	KHTML("KHTML", "khtml"),
	KONQEROR("Konqeror", "konqueror"),
	MIDP("MIDP", "MIDP");

	private final String name;
	private final String regex;

	BrowserEngine(String name, String regex) {
		this.name = name;
		this.regex = regex;
	}

	public String getName() {
		return name;
	}

	public String getRegex() {
		return regex;
	}

	public static BrowserEngine parse(@NonNull String info) {
		for (BrowserEngine browserEngine : BrowserEngine.values()) {
			if (RegexUtils.find(browserEngine.getRegex(), info)) {
				return browserEngine;
			}
		}
		return null;
	}
}
