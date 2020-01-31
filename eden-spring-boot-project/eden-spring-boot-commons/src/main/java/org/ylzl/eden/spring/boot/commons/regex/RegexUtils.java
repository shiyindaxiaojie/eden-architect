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
package org.ylzl.eden.spring.boot.commons.regex;

import lombok.experimental.UtilityClass;
import lombok.experimental.UtilityClass;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式工具类
 *
 * @author gyl
 * @since 0.0.1
 */
@UtilityClass
public class RegexUtils {

    public static boolean isMatch(@NonNull String regex, @NonNull CharSequence input) {
        return Pattern.matches(regex, input);
    }

    public static boolean find(@NonNull String regex, @NonNull CharSequence input) {
        return Pattern.compile(regex, Pattern.MULTILINE).matcher(input).find();
    }

    public static List<String> addWithGroup(@NonNull String regex, @NonNull CharSequence input) {
        List<String> matches = new ArrayList<>();
        Matcher matcher = Pattern.compile(regex).matcher(input);
        while (matcher.find()) {
            matches.add(matcher.group());
        }
        return matches;
    }

    public static String replaceAll(@NonNull String regex, @NonNull CharSequence input, @NonNull String replacement) {
        return Pattern.compile(regex).matcher(input).replaceAll(replacement);
    }

    public static String replaceFirst(@NonNull String regex, @NonNull CharSequence input, @NonNull String replacement) {
        return Pattern.compile(regex).matcher(input).replaceFirst(replacement);
    }
}
