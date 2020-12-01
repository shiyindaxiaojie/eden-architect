package org.ylzl.eden.spring.boot.commons.exception;

import java.text.MessageFormat;
import java.util.Collection;

/**
 * 断言工具集
 *
 * @author gyl
 * @since 2.0.0
 */
public class Assert extends org.springframework.util.Assert {

  public static void state(boolean expression, String pattern, Object... args) {
    state(expression, MessageFormat.format(pattern, args));
  }

  public static void notNull(Object object, String pattern, Object... args) {
    notNull(object, MessageFormat.format(pattern, args));
  }

  public static void isNull(Object object, String pattern, Object... args) {
    isNull(object, MessageFormat.format(pattern, args));
  }

  public static void hasLength(String text, String pattern, Object... args) {
    hasLength(text, MessageFormat.format(pattern, args));
  }

  public static void hasText(String text, String pattern, Object... args) {
    hasText(text, MessageFormat.format(pattern, args));
  }

  public static void notEmpty(Object[] array, String pattern, Object... args) {
    notEmpty(array, MessageFormat.format(pattern, args));
  }

  public static void notEmpty(Collection<?> collection, String pattern, Object... args) {
    notEmpty(collection, MessageFormat.format(pattern, args));
  }

  public static void noNullElements(Object[] array, String pattern, Object... args) {
    noNullElements(array, MessageFormat.format(pattern, args));
  }
}
