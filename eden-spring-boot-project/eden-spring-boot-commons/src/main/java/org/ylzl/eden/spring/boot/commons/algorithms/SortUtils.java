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

package org.ylzl.eden.spring.boot.commons.algorithms;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

/**
 * 排序工具集
 *
 * @author gyl
 * @since 1.0.0
 */
@UtilityClass
public class SortUtils {

	/**
	 * 交换数据元素
	 *
	 * @param array 交换数组
	 * @param idx   第一个元素的索引
	 * @param idy   第二个元素的索引
	 */
	public static <T> boolean swap(@NonNull T[] array, int idx, int idy) {
		T swap = array[idx];
		array[idx] = array[idy];
		array[idy] = swap;
		return true;
	}

	/**
	 * 检查第一个元素是否小于另一个元素
	 *
	 * @param v 第一个元素
	 * @param w 第二个元素
	 * @return 如果第一个元素小于第二个元素，则为 true
	 */
	public static <T extends Comparable<T>> boolean less(@NonNull T v, @NonNull T w) {
		return v.compareTo(w) < 0;
	}

	/**
	 * 对调数据元素
	 *
	 * @param array 对调数组
	 * @param left  数组左侧索引
	 * @param right 数组右侧索引
	 */
	public static <T extends Comparable<T>> void flip(@NonNull T[] array, int left, int right) {
		while (left <= right) {
			swap(array, left++, right--);
		}
	}
}
