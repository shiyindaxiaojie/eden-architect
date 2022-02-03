package org.ylzl.eden.spring.framework.web.errors;

import lombok.NonNull;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Map;

/**
 * 针对可能发生的错误进行断言
 *
 * @author gyl
 * @since 2.4.x
 */
public interface AssertError extends Error {

	void throwNewException(Throwable e);

	default void doesNotContain(@NonNull String textToSearch, String substring) {
		try {
			Assert.doesNotContain(textToSearch, substring, getErrMessage());
		} catch (IllegalArgumentException e) {
			throwNewException(e);
		}
	}

	default void hasLength(@NonNull String expression) {
		try {
			Assert.hasLength(expression, getErrMessage());
		} catch (IllegalArgumentException e) {
			throwNewException(e);
		}
	}

	default void hasText(String text) {
		try {
			Assert.hasText(text, getErrMessage());
		} catch (IllegalArgumentException e) {
			throwNewException(e);
		}
	}

	default void isInstanceOf(Class<?> type, @NonNull Object obj) {
		try {
			Assert.isInstanceOf(type, obj, getErrMessage());
		} catch (IllegalArgumentException e) {
			throwNewException(e);
		}
	}

	default void isNull(Object object) {
		try {
			Assert.isNull(object, getErrMessage());
		} catch (IllegalArgumentException e) {
			throwNewException(e);
		}
	}

	default void notNull(Object object) {
		try {
			Assert.notNull(object, getErrMessage());
		} catch (IllegalArgumentException e) {
			throwNewException(e);
		}
	}

	default void isTrue(boolean expression) {
		try {
			Assert.isTrue(expression, getErrMessage());
		} catch (IllegalArgumentException e) {
			throwNewException(e);
		}
	}

	default void noNullElements(@NonNull Collection<?> collection) {
		try {
			Assert.noNullElements(collection, getErrMessage());
		} catch (IllegalArgumentException e) {
			throwNewException(e);
		}
	}

	default void notEmpty(@NonNull Object[] array) {
		try {
			Assert.notEmpty(array, getErrMessage());
		} catch (IllegalArgumentException e) {
			throwNewException(e);
		}
	}

	default void notEmpty(@NonNull Collection<?> collection) {
		try {
			Assert.notEmpty(collection, getErrMessage());
		} catch (IllegalArgumentException e) {
			throwNewException(e);
		}
	}

	default void notEmpty(@NonNull Map<?, ?> map) {
		try {
			Assert.notEmpty(map, getErrMessage());
		} catch (IllegalArgumentException e) {
			throwNewException(e);
		}
	}

	default void isAssignable(Class<?> superType, @NonNull Class<?> subType) {
		try {
			Assert.isAssignable(superType, subType, getErrMessage());
		} catch (IllegalArgumentException e) {
			throwNewException(e);
		}
	}

	default void state(boolean expression) {
		try {
			Assert.state(expression, getErrMessage());
		} catch (IllegalArgumentException e) {
			throwNewException(e);
		}
	}
}
