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

package org.ylzl.eden.spring.boot.commons.algorithms.consistent;

import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;
import lombok.Getter;
import lombok.NonNull;

/**
 * 一致性哈希
 *
 * @author gyl
 * @since 0.0.1
 */
public class ConsistentHash<T> {

	/**
	 * 虚拟节点个数
	 */
	private final int numberOfReplicas;

	/**
	 * 环形存储虚拟节点的哈希值到真实节点的映射
	 * <br> 如果是 JDK1.8 使用 <code>ConcurrentSkipListMap</code> 更好
	 */
	@Getter
	private final SortedMap<Integer, T> circle = new TreeMap<Integer, T>();

	/**
	 * 构造函数
	 *
	 * @param numberOfReplicas 虚拟节点个数
	 */
	public ConsistentHash(int numberOfReplicas) {
		this.numberOfReplicas = numberOfReplicas;
	}

	/**
	 * 构造函数
	 *
	 * @param numberOfReplicas 虚拟节点个数
	 * @param nodes            机器节点
	 */
	public ConsistentHash(int numberOfReplicas, @NonNull Collection<T> nodes) {
		this.numberOfReplicas = numberOfReplicas;
		for (T node : nodes) {
			add(node);
		}
	}

	/**
	 * 新增机器节点
	 *
	 * @param node 节点实例
	 */
	public void add(@NonNull T node) {
		for (int i = 0; i < numberOfReplicas; i++) {
			String nodestr = node.toString() + i;
			int hash = hash(nodestr);
			// 不同的虚拟节点有不同的哈希值，但都对应同一个实际机器节点
			circle.put(hash, node);
		}
	}

	/**
	 * 移除机器节点
	 *
	 * @param node 节点实例
	 */
	public void remove(@NonNull T node) {
		for (int i = 0; i < numberOfReplicas; i++) {
			circle.remove(hash(node.toString() + i));
		}
	}

	/**
	 * 获得一个最近的顺时针节点，根据给定的 key 获取哈希值，然后再取顺时针方向上最近的一个虚拟节点对应的实际节点获取数据
	 */
	public T get(@NonNull String key) {
		if (circle.isEmpty()) {
			return null;
		}
		int hash = hash(key);
		if (!circle.containsKey(hash)) {
			// 通过 tailMap 获取大于等于 hash 后面的映射获取最近第一个值
			SortedMap<Integer, T> tailMap = circle.tailMap(hash);
			hash = tailMap.isEmpty() ? circle.firstKey() : tailMap.firstKey();
		}
		return circle.get(hash);
	}

	/**
	 * 使用 FNV1_32_HASH 算法计算哈希值
	 *
	 * @param str
	 * @return
	 */
	private static int hash(String str) {
		final int p = 16777619;
		int hash = (int) 2166136261L;
		for (int i = 0; i < str.length(); i++) {
			hash = (hash ^ str.charAt(i)) * p;
		}
		hash += hash << 13;
		hash ^= hash >> 7;
		hash += hash << 3;
		hash ^= hash >> 17;
		hash += hash << 5;

		// 如果算出来的值为负数则取其绝对值
		if (hash < 0) {
			hash = Math.abs(hash);
		}
		return hash;
	}
}
