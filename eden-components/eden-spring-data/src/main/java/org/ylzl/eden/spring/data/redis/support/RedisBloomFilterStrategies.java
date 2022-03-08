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

package org.ylzl.eden.spring.data.redis.support;

import com.google.common.hash.Funnel;
import com.google.common.hash.Hashing;
import com.google.common.primitives.Longs;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Redis 布隆过滤器策略
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
enum RedisBloomFilterStrategies implements RedisBloomFilter.Strategy {
	MURMUR128_MITZ_32() {
		@Override
		public <T> boolean put(
			RedisTemplate<String, T> redisTemplate,
			String key,
			T value,
			int numHashFunctions,
			Funnel<? super T> funnel) {
			long hash64 = Hashing.murmur3_128().hashObject(value, funnel).asLong();
			int hash1 = (int) hash64;
			int hash2 = (int) (hash64 >>> 32);

			for (int i = 1; i <= numHashFunctions; i++) {
				int combinedHash = hash1 + (i * hash2); // 因为用的是同一个 hash 函数，所以通过 i * hash2 生成不同的 hash 合并值
				if (combinedHash < 0) {
					combinedHash = ~combinedHash;
				}
				if (!redisTemplate
					.opsForValue()
					.setBit(key, combinedHash, true)) { // 通过合并 hash 值对 bit 的长度取模，所以数组的下标只能在 [0, bitSize) 区间
					return false;
				}
			}
			return true;
		}

		@Override
		public <T> boolean mightContain(
			RedisTemplate<String, T> redisTemplate,
			String key,
			T value,
			int numHashFunctions,
			Funnel<? super T> funnel) {
			long hash64 = Hashing.murmur3_128().hashObject(value, funnel).asLong();
			int hash1 = (int) hash64;
			int hash2 = (int) (hash64 >>> 32);

			for (int i = 1; i <= numHashFunctions; i++) {
				int combinedHash = hash1 + (i * hash2);
				if (combinedHash < 0) {
					combinedHash = ~combinedHash;
				}
				if (!redisTemplate.opsForValue().getBit(key, combinedHash)) {
					return false;
				}
			}
			return true;
		}
	},
	MURMUR128_MITZ_64() {
		@Override
		public <T> boolean put(
			RedisTemplate<String, T> redisTemplate,
			String key,
			T value,
			int numHashFunctions,
			Funnel<? super T> funnel) {
			byte[] bytes = Hashing.murmur3_128().hashObject(value, funnel).asBytes();
			long hash1 = lowerEight(bytes);
			long hash2 = upperEight(bytes);

			boolean bitsChanged = false;
			long combinedHash = hash1;
			for (int i = 0; i < numHashFunctions; i++) {
				if (!redisTemplate
					.opsForValue()
					.setBit(key, combinedHash, true)) { // 通过合并 hash 值对 bit 的长度取模，所以数组的下标只能在 [0, bitSize) 区间
					return false;
				}
				combinedHash += hash2;
			}
			return bitsChanged;
		}

		@Override
		public <T> boolean mightContain(
			RedisTemplate<String, T> redisTemplate,
			String key,
			T value,
			int numHashFunctions,
			Funnel<? super T> funnel) {
			byte[] bytes = Hashing.murmur3_128().hashObject(value, funnel).asBytes();
			long hash1 = lowerEight(bytes);
			long hash2 = upperEight(bytes);

			long combinedHash = hash1;
			for (int i = 0; i < numHashFunctions; i++) {
				if (!redisTemplate.opsForValue().getBit(key, combinedHash)) {
					return false;
				}
				combinedHash += hash2;
			}
			return true;
		}

		private long lowerEight(byte[] bytes) {
			return Longs.fromBytes(
				bytes[7], bytes[6], bytes[5], bytes[4], bytes[3], bytes[2], bytes[1], bytes[0]);
		}

		private long upperEight(byte[] bytes) {
			return Longs.fromBytes(
				bytes[15], bytes[14], bytes[13], bytes[12], bytes[11], bytes[10], bytes[9], bytes[8]);
		}
	};
}
