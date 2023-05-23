/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ylzl.eden.commons.lang;

import lombok.experimental.UtilityClass;

/**
 * 哈希工具集
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @see cn.hutool.core.util.HashUtil
 * @since 2.4.x
 */
@UtilityClass
public class HashUtils {

	/**
	 * Fowler-Noll-Vo (FNV-1a 版本)
	 *
	 * @param key 字符串
	 * @return 哈希值
	 */
	public static int fnv1a(String key) {
		int hash = (int) 2_166_136_261L; // offset_basis
		int prime = 16777619; // 32 bit FNV_prime = 2^24 + 2^8 + 0x93 = 16777619
		int len = key.length();
		for (int i = 0; i < len; i++) {
			hash = (hash ^ key.charAt(i)) * prime;
		}
		hash += hash << 13;
		hash ^= hash >> 7;
		hash += hash << 3;
		hash ^= hash >> 17;
		hash += hash << 5;
		return Math.abs(hash);
	}

	/**
	 * Fowler-Noll-Vo (FNV-1 版本)
	 *
	 * @param key 字符串
	 * @return 哈希值
	 */
	public static int fnv1(String key) {
		int hash = (int) 2_166_136_261L; // offset_basis
		int prime = 16777619; // 32 bit FNV_prime = 2^24 + 2^8 + 0x93 = 16777619
		int len = key.length();
		for (int i = 0; i < len; i++) {
			hash = (hash * prime) ^ key.charAt(i);
		}
		hash += hash << 13;
		hash ^= hash >> 7;
		hash += hash << 3;
		hash ^= hash >> 17;
		hash += hash << 5;
		return Math.abs(hash);
	}

	/**
	 * DJBX33A (Daniel J. Bernstein, Times 33 with Addition)
	 *
	 * @param key 字符串
	 * @return 哈希值
	 */
	public static int bernstein(String key) {
		int hash = 0;
		int i;
		for (i = 0; i < key.length(); i++) {
			hash = 33 * hash + key.charAt(i);
		}
		return hash;
	}

	/**
	 * Universal Hashing (全域哈希)
	 *
	 * @param key  字符串
	 * @param mask 掩码
	 * @param tab  偏移值
	 * @return 哈希值
	 */
	public static int universal(String key, int mask, int[] tab) {
		int hash = 0;
		int len = key.length();
		for (int i = 0; i < (len << 3); i += 8) {
			char k = key.charAt(i >> 3);
			if ((k & 0x01) == 0) {
				hash ^= tab[i];
			}
			if ((k & 0x02) == 0) {
				hash ^= tab[i + 1];
			}
			if ((k & 0x04) == 0) {
				hash ^= tab[i + 2];
			}
			if ((k & 0x08) == 0) {
				hash ^= tab[i + 3];
			}
			if ((k & 0x10) == 0) {
				hash ^= tab[i + 4];
			}
			if ((k & 0x20) == 0) {
				hash ^= tab[i + 5];
			}
			if ((k & 0x40) == 0) {
				hash ^= tab[i + 6];
			}
			if ((k & 0x80) == 0) {
				hash ^= tab[i + 7];
			}
		}
		return (hash & mask);
	}

	/**
	 * Zobrist Hashing (佐布里斯特散列)
	 *
	 * @param key  字符串
	 * @param mask 掩码
	 * @param tab  偏移值
	 * @return 哈希值
	 */
	public static int zobrist(String key, int mask, int[][] tab) {
		int hash = 0;
		int len = key.length();
		for (int i = 0; i < len; ++i) {
			hash ^= tab[i][key.charAt(i)];
		}
		return (hash & mask);
	}

	/**
	 * RS Hashing
	 *
	 * @param key 字符串
	 * @return 哈希值
	 */
	public static int rs(String key) {
		int hash = 0;
		int len = key.length();
		int b = 378551;
		int a = 63689;
		for (int i = 0; i < len; i++) {
			hash = hash * a + key.charAt(i);
			a = a * b;
		}
		return hash & 0x7FFFFFFF;
	}

	/**
	 * JS Hashing
	 *
	 * @param key 字符串
	 * @return 哈希值
	 */
	public static int js(String key) {
		int hash = 1315423911;
		int len = key.length();
		for (int i = 0; i < len; i++) {
			hash ^= ((hash << 5) + key.charAt(i) + (hash >> 2));
		}
		return hash & 0x7FFFFFFF;
	}

	/**
	 * PJW Hashing
	 *
	 * @param key 字符串
	 * @return 哈希值
	 */
	public static int pjw(String key) {
		int hash = 0;
		int len = key.length();
		int bitsInUnsignedInt = 32;
		int threeQuarters = (bitsInUnsignedInt * 3) / 4;
		int oneEighth = bitsInUnsignedInt / 8;
		int highBits = 0xFFFFFFFF << (bitsInUnsignedInt - oneEighth);
		int temp;
		for (int i = 0; i < len; i++) {
			hash = (hash << oneEighth) + key.charAt(i);
			if ((temp = hash & highBits) != 0) {
				hash = ((hash ^ (temp >> threeQuarters)) & (~highBits));
			}
		}
		return hash & 0x7FFFFFFF;
	}

	/**
	 * ELF Hashing
	 *
	 * @param key 字符串
	 * @return 哈希值
	 */
	public static int elf(String key) {
		int hash = 0;
		int len = key.length();
		int x;
		for (int i = 0; i < len; i++) {
			hash = (hash << 4) + key.charAt(i);
			if ((x = (int) (hash & 0xF0000000L)) != 0) {
				hash ^= (x >> 24);
				hash &= ~x;
			}
		}
		return hash & 0x7FFFFFFF;
	}

	/**
	 * BKDR Hashing
	 *
	 * @param key 字符串
	 * @return 哈希值
	 */
	public static int bkdr(String key) {
		int hash = 0;
		int len = key.length();
		int seed = 131;
		for (int i = 0; i < len; i++) {
			hash = (hash * seed) + key.charAt(i);
		}
		return hash & 0x7FFFFFFF;
	}

	/**
	 * SDBM Hashing
	 *
	 * @param key 字符串
	 * @return 哈希值
	 */
	public static int sdbm(String key) {
		int hash = 0;
		int len = key.length();
		for (int i = 0; i < len; i++) {
			hash = key.charAt(i) + (hash << 6) + (hash << 16) - hash;
		}
		return hash & 0x7FFFFFFF;
	}

	/**
	 * DJB Hashing
	 *
	 * @param key 字符串
	 * @return 哈希值
	 */
	public static int djb(String key) {
		int hash = 5381;
		int len = key.length();
		for (int i = 0; i < len; i++) {
			hash = ((hash << 5) + hash) + key.charAt(i);
		}
		return hash & 0x7FFFFFFF;
	}

	/**
	 * DEK Hashing
	 *
	 * @param key 字符串
	 * @return 哈希值
	 */
	public static int dek(String key) {
		int hash = 0;
		int len = key.length();
		for (int i = 0; i < len; i++) {
			hash = ((hash << 5) ^ (hash >> 27)) ^ key.charAt(i);
		}
		return hash & 0x7FFFFFFF;
	}

	/**
	 * AP Hashing
	 *
	 * @param key 字符串
	 * @return 哈希值
	 */
	public static int ap(String key) {
		int hash = 0;
		int len = key.length();
		for (int i = 0; i < len; i++) {
			hash ^= ((i & 1) == 0) ?
				((hash << 7) ^ key.charAt(i) ^ (hash >> 3)) :
				(~((hash << 11) ^ key.charAt(i) ^ (hash >> 5)));
		}
		return hash & 0x7FFFFFFF;
	}
}
