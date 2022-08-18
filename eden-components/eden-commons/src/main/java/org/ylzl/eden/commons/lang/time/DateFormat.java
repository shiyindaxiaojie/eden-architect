/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ylzl.eden.commons.lang.time;

import lombok.Getter;
import org.apache.commons.lang3.time.FastDateFormat;

/**
 * 日期格式化
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Getter
public enum DateFormat {
	ISO_8601_EXTENDED_DATE_FORMAT(DateFormat.ISO_8601_EXTENDED_DATE),
	ISO_8601_EXTENDED_TIME_FORMAT(DateFormat.ISO_8601_EXTENDED_TIME),
	ISO_8601_EXTENDED_DATETIME_FORMAT(DateFormat.ISO_8601_EXTENDED_DATETIME),
	ISO_8601_EXTENDED_DATE_NO_HYPHEN_FORMAT(DateFormat.ISO_8601_EXTENDED_DATE_NO_HYPHEN),
	ISO_8601_EXTENDED_TIME_NO_HYPHEN_FORMAT(DateFormat.ISO_8601_EXTENDED_TIME_NO_HYPHEN),
	ISO_8601_EXTENDED_DATETIME_NO_HYPHEN_FORMAT(DateFormat.ISO_8601_EXTENDED_DATETIME_NO_HYPHEN),
	ISO_DATETIME_TIME_ZONE_FORMAT(DateFormat.ISO_DATETIME_TIME_ZONE);

	public static final String ISO_8601_EXTENDED_DATE = "yyyy-MM-dd";
	public static final String ISO_8601_EXTENDED_TIME = "HH:mm:ss";
	public static final String ISO_8601_EXTENDED_DATETIME = "yyyy-MM-dd HH:mm:ss";
	public static final String ISO_8601_EXTENDED_DATE_NO_HYPHEN = "yyyyMMdd";
	public static final String ISO_8601_EXTENDED_TIME_NO_HYPHEN = "HHmmss";
	public static final String ISO_8601_EXTENDED_DATETIME_NO_HYPHEN = "yyyyMMddHHmmss";
	public static final String ISO_DATETIME_TIME_ZONE = "yyyy-MM-dd'T'HH:mm:ss'Z'";

	public static final String DEFAULT_TIME_ZONE = "GMT+8";

	private final String pattern;

	private final FastDateFormat fastDateFormat;

	DateFormat(String pattern) {
		this.pattern = pattern;
		this.fastDateFormat = FastDateFormat.getInstance(pattern);
	}

	public static FastDateFormat getFastDateFormat(String pattern) {
		for (DateFormat dateFormat : DateFormat.values()) {
			if (dateFormat.getPattern().equals(pattern)) {
				return dateFormat.getFastDateFormat();
			}
		}
		throw new UnsupportedOperationException();
	}
}
