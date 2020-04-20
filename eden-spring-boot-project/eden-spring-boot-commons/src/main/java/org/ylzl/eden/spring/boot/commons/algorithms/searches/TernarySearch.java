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

package org.ylzl.eden.spring.boot.commons.algorithms.searches;

/**
 * 三元查找
 *
 * <p>时间复杂度：最好为 O(1)，最差为 O(log3(N))，平均为 O(log3(N))</p>
 * <p>空间复杂度：最差为 O(1)</p>
 *
 * @author gyl
 * @since 0.0.1
 */
public class TernarySearch implements Search {

	/**
	 * 查找
	 *
	 * @param array 数组
	 * @param key   查找关键字
	 * @return 匹配值
	 */
	@Override
	public <T extends Comparable<T>> int search(T[] array, T key) {
		return search(array, key, 0, array.length - 1);
	}

	private <T extends Comparable<T>> int search(T[] arr, T key, int start, int end) {
		if (start > end) {
			return -1;
		}

		int mid1 = start + (end - start) / 3;
		int mid2 = start + 2 * (end - start) / 3;
		if (key.compareTo(arr[mid1]) == 0) {
			return mid1;
		}
		if (key.compareTo(arr[mid2]) == 0) {
			return mid2;
		}
		if (key.compareTo(arr[mid1]) < 0) {
			return search(arr, key, start, --mid1);
		}
		if (key.compareTo(arr[mid2]) > 0) {
			return search(arr, key, ++mid2, end);
		}
		return search(arr, key, mid1, mid2);
	}
}
