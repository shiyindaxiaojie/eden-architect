package org.ylzl.eden.commons.id;

import lombok.experimental.UtilityClass;

import java.security.SecureRandom;
import java.util.Random;

/**
 * NanoId 工具集
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@UtilityClass
public class NanoIdUtils {

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
