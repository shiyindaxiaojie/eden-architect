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

package org.ylzl.eden.commons.id;

import lombok.experimental.UtilityClass;

import java.security.SecureRandom;
import java.util.Random;

/**
 * NanoId 生成器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@UtilityClass
public class NanoIdGenerator {

	public static final SecureRandom DEFAULT_NUMBER_GENERATOR = new SecureRandom();

	public static final char[] DEFAULT_ALPHABET = "_-0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

	public static final int DEFAULT_SIZE = 21;

	public static String randomNanoId() {
		return randomNanoId(DEFAULT_NUMBER_GENERATOR, DEFAULT_ALPHABET, DEFAULT_SIZE);
	}

	public static String randomNanoId(final Random random, final char[] alphabet, final int size) {
		if (random == null) {
			throw new IllegalArgumentException("random cannot be null.");
		}
		if (alphabet == null) {
			throw new IllegalArgumentException("alphabet cannot be null.");
		}
		if (alphabet.length == 0 || alphabet.length >= 256) {
			throw new IllegalArgumentException("alphabet must contain between 1 and 255 symbols.");
		}
		if (size <= 0) {
			throw new IllegalArgumentException("size must be greater than zero.");
		}

		final int mask = (2 << (int) Math.floor(Math.log(alphabet.length - 1) / Math.log(2))) - 1;
		final int step = (int) Math.ceil(1.6 * mask * size / alphabet.length);

		final StringBuilder idBuilder = new StringBuilder();
		while (true) {
			final byte[] bytes = new byte[step];
			random.nextBytes(bytes);
			for (int i = 0; i < step; i++) {
				final int alphabetIndex = bytes[i] & mask;
				if (alphabetIndex < alphabet.length) {
					idBuilder.append(alphabet[alphabetIndex]);
					if (idBuilder.length() == size) {
						return idBuilder.toString();
					}
				}
			}
		}
	}
}
