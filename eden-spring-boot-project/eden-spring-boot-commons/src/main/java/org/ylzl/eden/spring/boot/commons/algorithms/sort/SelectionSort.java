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

package org.ylzl.eden.spring.boot.commons.algorithms.sort;

import lombok.NonNull;
import org.ylzl.eden.spring.boot.commons.algorithms.SortUtils;

/**
 * 选择排序
 *
 * @author gyl
 * @since 1.0.0
 */
public class SelectionSort extends AbstractSort implements Sort {

	/**
	 * 排序数组
	 *
	 * @param unsorted - 未排序的数组
	 * @return 排序后的数组
	 */
	@Override
	public <T extends Comparable<T>> T[] sort(@NonNull T[] unsorted) {
		int n = unsorted.length;
		for (int i = 0; i < n - 1; i++) {
			int min = i;
			for (int j = i + 1; j < n; j++) {
				if (SortUtils.less(unsorted[j], unsorted[min])) {
					min = j;
				}
			}
			if (min != i) {
				SortUtils.swap(unsorted, i, min);
			}
		}
		return unsorted;
	}
}