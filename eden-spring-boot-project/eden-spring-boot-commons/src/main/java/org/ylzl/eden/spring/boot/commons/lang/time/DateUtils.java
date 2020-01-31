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

package org.ylzl.eden.spring.boot.commons.lang.time;

import lombok.NonNull;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具集
 *
 * @author gyl
 * @since 0.0.1
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    public static Date toDate(@NonNull String dateStr, @NonNull String format) throws ParseException {
        return new SimpleDateFormat(format).parse(dateStr);
    }

    public static Date toDate(@NonNull String dateStr) throws ParseException {
        return new SimpleDateFormat(DatePattern.NORM_DATETIME_PATTERN).parse(dateStr);
    }

    public static Date toDate(@NonNull Timestamp timestamp) {
        return new Date(timestamp.getTime());
    }

    public static Date toDate(@NonNull Calendar calendar) {
        return calendar.getTime();
    }

    public static java.sql.Date toSQLDate(@NonNull String dateStr, @NonNull String format) throws ParseException {
        return new java.sql.Date(toDate(dateStr, format).getTime());
    }

    public static java.sql.Date toSQLDate(@NonNull String dateStr) throws ParseException {
        return new java.sql.Date(toDate(dateStr).getTime());
    }

    public static java.sql.Date toSQLDate(@NonNull Date date) {
        return new java.sql.Date(date.getTime());
    }

    public static String toDateString(@NonNull Date date, @NonNull String format) {
        return new SimpleDateFormat(format).format(date);
    }

    public static String toDateString(@NonNull Date date) {
        return new SimpleDateFormat(DatePattern.NORM_DATE_PATTERN).format(date);
    }

    public static String toDateTimeString(@NonNull Date date) {
        return new SimpleDateFormat(DatePattern.NORM_DATETIME_PATTERN).format(date);
    }

    public static String toDateString(@NonNull Calendar calendar, @NonNull String format) {
        return toDateString(calendar.getTime(), format);
    }

    public static String toDateString(@NonNull Calendar calendar) {
        return toDateString(calendar.getTime());
    }

    public static Calendar toCalendar(@NonNull String dateStr, @NonNull String format) throws ParseException {
        return toCalendar(toDate(dateStr, format));
    }

    public static Calendar toCalendar(@NonNull String dateStr) throws ParseException {
        return toCalendar(toDate(dateStr));
    }

    public static Calendar toCalendar(@NonNull Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public static Timestamp toTimestamp(@NonNull String dateStr, @NonNull String format) throws ParseException {
        return toTimestamp(toDate(dateStr, format));
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

    public static String getDateTime() {
        return toDateString(new Date(), DatePattern.NORM_DATETIME_PATTERN);
    }

    public static Date dateAdd(@NonNull Date date, @NonNull int amount, @NonNull int calendarType) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        switch (calendarType) {
            case Calendar.SECOND:
                calendar.add(Calendar.SECOND, amount);
                break;

            case Calendar.MINUTE:
                calendar.add(Calendar.MINUTE, amount);
                break;

            case Calendar.HOUR:
                calendar.add(Calendar.HOUR, amount);
                break;

            case Calendar.DATE:
                calendar.add(Calendar.DATE, amount);
                break;

            case Calendar.MONTH:
                calendar.add(Calendar.MONTH, amount);
                break;

            case Calendar.YEAR:
                calendar.add(Calendar.YEAR, amount);
                break;

            default:
                throw new RuntimeException("不支持的 Calendar 类型");
        }
        return calendar.getTime();
    }
}
