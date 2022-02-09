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
 * 快速排序
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class QuickSort extends AbstractSort implements Sort {

	private static <T extends Comparable<T>> void doSort(T[] array, int left, int right) {
		if (left < right) {
			int pivot = randomPartition(array, left, right);
			doSort(array, left, pivot - 1);
			doSort(array, pivot, right);
		}
	}

	private static <T extends Comparable<T>> int randomPartition(T[] array, int left, int right) {
		int randomIndex = left + (int) (Math.random() * (right - left + 1));
		SortUtils.swap(array, randomIndex, right);
		return partition(array, left, right);
	}

	private static <T extends Comparable<T>> int partition(T[] array, int left, int right) {
		int mid = (left + right) / 2;
		T pivot = array[mid];
		while (left <= right) {
			while (SortUtils.less(array[left], pivot)) {
				++left;
			}
			while (SortUtils.less(pivot, array[right])) {
				--right;
			}
			if (left <= right) {
				SortUtils.swap(array, left, right);
				++left;
				--right;
			}
		}
		return left;
	}

	/**
	 * 排序数组
	 *
	 * @param array - 未排序的数组
	 * @return 排序后的数组
	 */
	@Override
	public <T extends Comparable<T>> T[] sort(@NonNull T[] array) {
		doSort(array, 0, array.length - 1);
		return array;
	}
}
