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

package org.ylzl.eden.spring.boot.commons.lang.math;

import lombok.experimental.UtilityClass;
import org.ylzl.eden.spring.boot.commons.lang.StringConstants;

import java.util.HashMap;
import java.util.Map;

/**
 * 罗马数字工具集
 *
 * @author gyl
 * @since 0.0.1
 */
@UtilityClass
public class RomanUtils {

  private static int[] allArabianRomanNumbers =
      new int[] {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};

  private static String[] allRomanNumbers =
      new String[] {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};

  private static Map<Character, Integer> map =
      new HashMap<Character, Integer>() {
        {
          put('I', 1);
          put('V', 5);
          put('X', 10);
          put('L', 50);
          put('C', 100);
          put('D', 500);
          put('M', 1000);
        }
      };

  public static String integerToRoman(int num) {
    if (num <= 0) {
      return StringConstants.EMPTY;
    }

    StringBuilder builder = new StringBuilder();
    for (int a = 0; a < allArabianRomanNumbers.length; a++) {
      int times = num / allArabianRomanNumbers[a];
      for (int b = 0; b < times; b++) {
        builder.append(allRomanNumbers[a]);
      }
      num -= times * allArabianRomanNumbers[a];
    }
    return builder.toString();
  }

  public static int romanToInteger(String roman) {
    char prev = ' ';
    int sum = 0;
    int newPrev = 0;
    for (int i = roman.length() - 1; i >= 0; i--) {
      char c = roman.charAt(i);
      if (prev != ' ') {
        newPrev = map.get(prev) > newPrev ? map.get(prev) : newPrev;
      }
      int currentNum = map.get(c);
      if (currentNum >= newPrev) {
        sum += currentNum;
      } else {
        sum -= currentNum;
      }
      prev = c;
    }
    return sum;
  }
}
