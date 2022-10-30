package org.ylzl.eden.common.cache.integration.l1cache.caffeine;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.errorprone.annotations.FormatMethod;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.requireNonNull;


/**
 * 自定义 CaffeineSpec
 *
 * @see com.github.benmanes.caffeine.cache.CaffeineSpec
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Accessors(chain = true)
@Getter
@Setter
public class CustomCaffeineSpec {

	enum Strength { WEAK, SOFT }

	static final int UNSET_INT = -1;

	static final String SPLIT_OPTIONS = ",";
	static final String SPLIT_KEY_VALUE = "=";

	final String specification;

	int initialCapacity = UNSET_INT;
	long maximumWeight = UNSET_INT;
	long maximumSize = UNSET_INT;
	boolean recordStats;

	@Nullable Strength keyStrength;
	@Nullable Strength valueStrength;
	@Nullable Duration expireAfterWrite;
	@Nullable Duration expireAfterAccess;
	@Nullable Duration refreshAfterWrite;

	private CustomCaffeineSpec(String specification) {
		this.specification = requireNonNull(specification);
	}

	Caffeine<Object, Object> toBuilder() {
		Caffeine<Object, Object> builder = Caffeine.newBuilder();
		if (initialCapacity != UNSET_INT) {
			builder.initialCapacity(initialCapacity);
		}
		if (maximumSize != UNSET_INT) {
			builder.maximumSize(maximumSize);
		}
		if (maximumWeight != UNSET_INT) {
			builder.maximumWeight(maximumWeight);
		}
		if (keyStrength != null) {
			requireState(keyStrength == Strength.WEAK);
			builder.weakKeys();
		}
		if (valueStrength != null) {
			if (valueStrength == Strength.WEAK) {
				builder.weakValues();
			} else if (valueStrength == Strength.SOFT) {
				builder.softValues();
			} else {
				throw new IllegalStateException();
			}
		}
		if (expireAfterWrite != null) {
			builder.expireAfterWrite(expireAfterWrite);
		}
		if (expireAfterAccess != null) {
			builder.expireAfterAccess(expireAfterAccess);
		}
		if (refreshAfterWrite != null) {
			builder.refreshAfterWrite(refreshAfterWrite);
		}
		if (recordStats) {
			builder.recordStats();
		}
		return builder;
	}

	@SuppressWarnings("StringSplitter")
	public static @NonNull CustomCaffeineSpec parse(@NonNull String specification) {
		CustomCaffeineSpec spec = new CustomCaffeineSpec(specification);
		for (String option : specification.split(SPLIT_OPTIONS)) {
			spec.parseOption(option.trim());
		}
		return spec;
	}

	void parseOption(String option) {
		if (option.isEmpty()) {
			return;
		}

		@SuppressWarnings("StringSplitter")
		String[] keyAndValue = option.split(SPLIT_KEY_VALUE);
		requireArgument(keyAndValue.length <= 2,
			"key-value pair %s with more than one equals sign", option);

		String key = keyAndValue[0].trim();
		String value = (keyAndValue.length == 1) ? null : keyAndValue[1].trim();

		configure(key, value);
	}

	void configure(String key, @Nullable String value) {
		switch (key) {
			case "initialCapacity":
				initialCapacity(key, value);
				return;
			case "maximumSize":
				maximumSize(key, value);
				return;
			case "maximumWeight":
				maximumWeight(key, value);
				return;
			case "weakKeys":
				weakKeys(value);
				return;
			case "weakValues":
				valueStrength(key, value, Strength.WEAK);
				return;
			case "softValues":
				valueStrength(key, value, Strength.SOFT);
				return;
			case "expireAfterAccess":
				expireAfterAccess(key, value);
				return;
			case "expireAfterWrite":
				expireAfterWrite(key, value);
				return;
			case "refreshAfterWrite":
				refreshAfterWrite(key, value);
				return;
			case "recordStats":
				recordStats(value);
				return;
			default:
				throw new IllegalArgumentException("Unknown key " + key);
		}
	}

	void initialCapacity(String key, @Nullable String value) {
		requireArgument(initialCapacity == UNSET_INT,
			"initial capacity was already set to %,d", initialCapacity);
		initialCapacity = parseInt(key, value);
	}

	void maximumSize(String key, @Nullable String value) {
		requireArgument(maximumSize == UNSET_INT,
			"maximum size was already set to %,d", maximumSize);
		requireArgument(maximumWeight == UNSET_INT,
			"maximum weight was already set to %,d", maximumWeight);
		maximumSize = parseLong(key, value);
	}

	void maximumWeight(String key, @Nullable String value) {
		requireArgument(maximumWeight == UNSET_INT,
			"maximum weight was already set to %,d", maximumWeight);
		requireArgument(maximumSize == UNSET_INT,
			"maximum size was already set to %,d", maximumSize);
		maximumWeight = parseLong(key, value);
	}

	void weakKeys(@Nullable String value) {
		requireArgument(value == null, "weak keys does not take a value");
		requireArgument(keyStrength == null, "weak keys was already set");
		keyStrength = Strength.WEAK;
	}

	void valueStrength(String key, @Nullable String value, Strength strength) {
		requireArgument(value == null, "%s does not take a value", key);
		requireArgument(valueStrength == null, "%s was already set to %s", key, valueStrength);
		valueStrength = strength;
	}

	void expireAfterAccess(String key, @Nullable String value) {
		requireArgument(expireAfterAccess == null, "expireAfterAccess was already set");
		expireAfterAccess = parseDuration(key, value);
	}

	void expireAfterWrite(String key, @Nullable String value) {
		requireArgument(expireAfterWrite == null, "expireAfterWrite was already set");
		expireAfterWrite = parseDuration(key, value);
	}

	void refreshAfterWrite(String key, @Nullable String value) {
		requireArgument(refreshAfterWrite == null, "refreshAfterWrite was already set");
		refreshAfterWrite = parseDuration(key, value);
	}

	void recordStats(@Nullable String value) {
		requireArgument(value == null, "record stats does not take a value");
		requireArgument(!recordStats, "record stats was already set");
		recordStats = true;
	}

	static int parseInt(String key, @Nullable String value) {
		requireArgument((value != null) && !value.isEmpty(), "value of key %s was omitted", key);
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(String.format(
				"key %s value was set to %s, must be an integer", key, value), e);
		}
	}

	static long parseLong(String key, @Nullable String value) {
		requireArgument((value != null) && !value.isEmpty(), "value of key %s was omitted", key);
		try {
			return Long.parseLong(value);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(String.format(
				"key %s value was set to %s, must be a long", key, value), e);
		}
	}

	static Duration parseDuration(String key, @Nullable String value) {
		requireArgument((value != null) && !value.isEmpty(), "value of key %s omitted", key);

		@SuppressWarnings("NullAway")
		boolean isIsoFormat = value.contains("p") || value.contains("P");
		if (isIsoFormat) {
			Duration duration = Duration.parse(value);
			requireArgument(!duration.isNegative(),
				"key %s invalid format; was %s, but the duration cannot be negative", key, value);
			return duration;
		}

		@SuppressWarnings("NullAway")
		long duration = parseLong(key, value.substring(0, value.length() - 1));
		TimeUnit unit = parseTimeUnit(key, value);
		return Duration.ofNanos(unit.toNanos(duration));
	}

	static TimeUnit parseTimeUnit(String key, @Nullable String value) {
		requireArgument((value != null) && !value.isEmpty(), "value of key %s omitted", key);
		@SuppressWarnings("NullAway")
		char lastChar = Character.toLowerCase(value.charAt(value.length() - 1));
		switch (lastChar) {
			case 'd':
				return TimeUnit.DAYS;
			case 'h':
				return TimeUnit.HOURS;
			case 'm':
				return TimeUnit.MINUTES;
			case 's':
				return TimeUnit.SECONDS;
			default:
				throw new IllegalArgumentException(String.format(
					"key %s invalid format; was %s, must end with one of [dDhHmMsS]", key, value));
		}
	}

	@FormatMethod
	static void requireArgument(boolean expression, String template, @Nullable Object... args) {
		if (!expression) {
			throw new IllegalArgumentException(String.format(template, args));
		}
	}

	static void requireArgument(boolean expression) {
		if (!expression) {
			throw new IllegalArgumentException();
		}
	}

	static void requireState(boolean expression) {
		if (!expression) {
			throw new IllegalStateException();
		}
	}

	@FormatMethod
	static void requireState(boolean expression, String template, @Nullable Object... args) {
		if (!expression) {
			throw new IllegalStateException(String.format(template, args));
		}
	}

	public long getExpireInMs() {
		if (refreshAfterWrite != null) {
			return refreshAfterWrite.toMillis();
		}
		if (expireAfterWrite != null) {
			return expireAfterWrite.toMillis();
		}
		if (expireAfterAccess != null) {
			return expireAfterAccess.toMillis();
		}
		return UNSET_INT;
	}
}
