package org.ylzl.eden.spring.framework.cola.exception;

import lombok.NonNull;
import org.ylzl.eden.spring.framework.cola.exception.util.AssertEnhancer;

import java.util.Collection;
import java.util.Map;

/**
 * 针对可能发生的错误进行断言
 *
 * @author gyl
 * @since 2.4.x
 */
public interface ErrorAssert extends Error {

	void throwNewException(Throwable e);

	default void doesNotContain(@NonNull String textToSearch, String substring) {
		try {
			AssertEnhancer.doesNotContain(textToSearch, substring, getErrMessage());
		} catch (IllegalArgumentException e) {
			throwNewException(e);
		}
	}

	default void hasLength(@NonNull String expression) {
		try {
			AssertEnhancer.hasLength(expression, getErrMessage());
		} catch (IllegalArgumentException e) {
			throwNewException(e);
		}
	}

	default void hasText(String text) {
		try {
			AssertEnhancer.hasText(text, getErrMessage());
		} catch (IllegalArgumentException e) {
			throwNewException(e);
		}
	}

	default void isInstanceOf(Class<?> type, @NonNull Object obj) {
		try {
			AssertEnhancer.isInstanceOf(type, obj, getErrMessage());
		} catch (IllegalArgumentException e) {
			throwNewException(e);
		}
	}

	default void isNull(Object object) {
		try {
			AssertEnhancer.isNull(object, getErrMessage());
		} catch (IllegalArgumentException e) {
			throwNewException(e);
		}
	}

	default void notNull(Object object) {
		try {
			AssertEnhancer.notNull(object, getErrMessage());
		} catch (IllegalArgumentException e) {
			throwNewException(e);
		}
	}

	default void isTrue(boolean expression) {
		try {
			AssertEnhancer.isTrue(expression, getErrMessage());
		} catch (IllegalArgumentException e) {
			throwNewException(e);
		}
	}

	default void noNullElements(@NonNull Collection<?> collection) {
		try {
			AssertEnhancer.noNullElements(collection, getErrMessage());
		} catch (IllegalArgumentException e) {
			throwNewException(e);
		}
	}

	default void notEmpty(@NonNull Object[] array) {
		try {
			AssertEnhancer.notEmpty(array, getErrMessage());
		} catch (IllegalArgumentException e) {
			throwNewException(e);
		}
	}

	default void notEmpty(@NonNull Collection<?> collection) {
		try {
			AssertEnhancer.notEmpty(collection, getErrMessage());
		} catch (IllegalArgumentException e) {
			throwNewException(e);
		}
	}

	default void notEmpty(@NonNull Map<?, ?> map) {
		try {
			AssertEnhancer.notEmpty(map, getErrMessage());
		} catch (IllegalArgumentException e) {
			throwNewException(e);
		}
	}

	default void isAssignable(Class<?> superType, @NonNull Class<?> subType) {
		try {
			AssertEnhancer.isAssignable(superType, subType, getErrMessage());
		} catch (IllegalArgumentException e) {
			throwNewException(e);
		}
	}

	default void state(boolean expression) {
		try {
			AssertEnhancer.state(expression, getErrMessage());
		} catch (IllegalArgumentException e) {
			throwNewException(e);
		}
	}
}
