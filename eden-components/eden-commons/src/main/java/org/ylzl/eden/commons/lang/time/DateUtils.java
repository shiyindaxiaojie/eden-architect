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

package org.ylzl.eden.commons.lang.time;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具集
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@UtilityClass
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

	public static Date toDate(@NonNull String dateStr, @NonNull String pattern)
		throws ParseException {
		return DateFormat.parse(pattern).parse(dateStr);
	}

	public static Date toDate(@NonNull String dateStr) throws ParseException {
		return DateFormat.ISO_8601_EXTENDED_DATETIME_FORMAT.getFastDateFormat().parse(dateStr);
	}

	public static Date toDate(@NonNull Timestamp timestamp) {
		return new Date(timestamp.getTime());
	}

	public static Date toDate(@NonNull Calendar calendar) {
		return calendar.getTime();
	}

	public static java.sql.Date toSQLDate(@NonNull String dateStr, @NonNull String pattern)
		throws ParseException {
		return new java.sql.Date(toDate(dateStr, pattern).getTime());
	}

	public static java.sql.Date toSQLDate(@NonNull String dateStr) throws ParseException {
		return new java.sql.Date(toDate(dateStr).getTime());
	}

	public static java.sql.Date toSQLDate(@NonNull Date date) {
		return new java.sql.Date(date.getTime());
	}

	public static String toDateString(@NonNull Date date, @NonNull String pattern) {
		return DateFormat.parse(pattern).format(date);
	}

	public static String toDateString(@NonNull Date date) {
		return DateFormat.ISO_8601_EXTENDED_DATE_FORMAT.getFastDateFormat().format(date);
	}

	public static String toDateTimeString(@NonNull Date date) {
		return DateFormat.ISO_8601_EXTENDED_DATETIME_FORMAT.getFastDateFormat().format(date);
	}

	public static String toTimeString(@NonNull Date date) {
		return DateFormat.ISO_8601_EXTENDED_TIME_FORMAT.getFastDateFormat().format(date);
	}

	public static String toDateString(@NonNull Calendar calendar, @NonNull String pattern) {
		return toDateString(calendar.getTime(), pattern);
	}

	public static String toDateString(@NonNull Calendar calendar) {
		return toDateString(calendar.getTime());
	}

	public static Calendar toCalendar(@NonNull String dateStr, @NonNull String pattern)
		throws ParseException {
		return toCalendar(toDate(dateStr, pattern));
	}

	public static Calendar toCalendar(@NonNull String dateStr) throws ParseException {
		return toCalendar(toDate(dateStr));
	}

	public static Calendar toCalendar(@NonNull Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar;
	}

	public static Timestamp toTimestamp(@NonNull String dateStr, @NonNull String pattern)
		throws ParseException {
		return toTimestamp(toDate(dateStr, pattern));
	}

	public static Timestamp toTimestamp(@NonNull String dateStr) throws ParseException {
		return toTimestamp(toDate(dateStr));
	}

	public static Timestamp toTimestamp(@NonNull Date date) {
		return new Timestamp(date.getTime());
	}

	public static Timestamp toTimestamp(@NonNull Calendar calendar) {
		return toTimestamp(calendar.getTime());
	}

	public static String getDateTimeString() {
		return DateFormat.ISO_8601_EXTENDED_DATETIME_FORMAT
			.getFastDateFormat()
			.format(Calendar.getInstance());
	}
}
