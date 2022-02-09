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
 * 冒泡排序
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class BubbleSort extends AbstractSort implements Sort {

	/**
	 * 排序数组
	 *
	 * @param array - 未排序的数组
	 * @return 排序后的数组
	 */
	@Override
	public <T extends Comparable<T>> T[] sort(@NonNull T[] array) {
		for (int i = 0, size = array.length; i < size - 1; i++) {
			boolean swapped = false;
			for (int j = 0; j < size - 1 - i; j++) { // 因为最后一个是最大值，所以每 i 轮循环，j 比较的次数减 1
				if (SortUtils.less(array[j], array[j + 1])) {
					SortUtils.swap(array, j, j + 1);
					swapped = true;
				}
			}
			if (!swapped) {
				break;
			}
		}
		return array;
	}
}
