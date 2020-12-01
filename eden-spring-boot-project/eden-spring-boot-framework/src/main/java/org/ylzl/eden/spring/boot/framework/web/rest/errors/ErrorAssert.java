package org.ylzl.eden.spring.boot.framework.web.rest.errors;

import lombok.NonNull;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Map;

/**
 * 错误断言接口
 *
 * @author gyl
 * @since 0.0.1
 */
public interface ErrorAssert extends Error {

  default void doesNotContain(@NonNull String textToSearch, String substring) {
    try {
      Assert.doesNotContain(textToSearch, substring, getMessage());
    } catch (IllegalArgumentException e) {
      throw new BadRequestAlertException(getMessage(), e.getMessage());
    }
  }

  default void hasLength(@NonNull String expression) {
    try {
      Assert.hasLength(expression, getMessage());
    } catch (IllegalArgumentException e) {
      throw new BadRequestAlertException(getMessage(), e.getMessage());
    }
  }

  default void hasText(String text) {
    try {
      Assert.hasText(text, getMessage());
    } catch (IllegalArgumentException e) {
      throw new BadRequestAlertException(getMessage(), e.getMessage());
    }
  }

  default void isInstanceOf(Class<?> type, @NonNull Object obj) {
    try {
      Assert.isInstanceOf(type, obj, getMessage());
    } catch (IllegalArgumentException e) {
      throw new BadRequestAlertException(getMessage(), e.getMessage());
    }
  }

  default void isNull(Object object) {
    try {
      Assert.isNull(object, getMessage());
    } catch (IllegalArgumentException e) {
      throw new BadRequestAlertException(getMessage(), e.getMessage());
    }
  }

  default void isTrue(boolean expression) {
    try {
      Assert.isTrue(expression, getMessage());
    } catch (IllegalArgumentException e) {
      throw new BadRequestAlertException(getMessage(), e.getMessage());
    }
  }

  default void noNullElements(@NonNull Collection<?> collection) {
    try {
      Assert.noNullElements(collection, getMessage());
    } catch (IllegalArgumentException e) {
      throw new BadRequestAlertException(getMessage(), e.getMessage());
    }
  }

  default void notEmpty(@NonNull Object[] array) {
    try {
      Assert.notEmpty(array, getMessage());
    } catch (IllegalArgumentException e) {
      throw new BadRequestAlertException(getMessage(), e.getMessage());
    }
  }

  default void notEmpty(@NonNull Collection<?> collection) {
    try {
      Assert.notEmpty(collection, getMessage());
    } catch (IllegalArgumentException e) {
      throw new BadRequestAlertException(getMessage(), e.getMessage());
    }
  }

  default void notEmpty(@NonNull Map<?, ?> map) {
    try {
      Assert.notEmpty(map, getMessage());
    } catch (IllegalArgumentException e) {
      throw new BadRequestAlertException(getMessage(), e.getMessage());
    }
  }

  default void isAssignable(Class<?> superType, @NonNull Class<?> subType) {
    try {
      Assert.isAssignable(superType, subType, getMessage());
    } catch (IllegalArgumentException e) {
      throw new BadRequestAlertException(getMessage(), e.getMessage());
    }
  }

  default void state(boolean expression) {
    try {
      Assert.state(expression, getMessage());
    } catch (IllegalArgumentException e) {
      throw new BadRequestAlertException(getMessage(), e.getMessage());
    }
  }
}
