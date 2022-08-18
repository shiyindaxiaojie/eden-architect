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

package org.ylzl.eden.commons.algorithms.sorts;

import lombok.NonNull;
import org.ylzl.eden.commons.algorithms.SortUtils;

/**
 * 选择排序
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class SelectionSort extends AbstractSort implements Sort {

	public static void main(String[] args) {
		int[] array = {33, 1, 34, 543, 5, 13, 4, 82, 31};
		int len = array.length;
		int min;
		for (int i = 0; i < len - 1; i++) {
			min = i;
			for (int j = i + 1; j < len; j++) {
				if (array[j] < array[min]) {
					int temp = array[min];
					array[min] = array[j];
					array[j] = temp;
				}
			}
		}
		for (int i = 0; i < array.length; i++) {
			System.out.println(array[i]);
		}
	}

	/**
	 * 排序数组
	 *
	 * @param array - 未排序的数组
	 * @return 排序后的数组
	 */
	@Override
	public <T extends Comparable<T>> T[] sort(@NonNull T[] array) {
		int n = array.length;
		for (int i = 0; i < n - 1; i++) {
			int min = i;
			for (int j = i + 1; j < n; j++) { // j = i + 1 表示跳过前面比较后置顶的较小值
				if (SortUtils.less(array[j], array[min])) {
					min = j;
				}
			}
			if (min != i) {
				SortUtils.swap(array, i, min);
			}
		}
		return array;
	}
}
