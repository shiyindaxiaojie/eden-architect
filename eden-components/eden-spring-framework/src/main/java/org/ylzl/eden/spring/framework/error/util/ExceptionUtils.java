package org.ylzl.eden.spring.framework.error.util;

import lombok.experimental.UtilityClass;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

/**
 * 异常处理器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@UtilityClass
public class ExceptionUtils {

	/**
	 * 转换为运行时异常
	 *
	 * @param e
	 * @return
	 */
	public static RuntimeException unchecked(Throwable e) {
		if (e instanceof Error) {
			throw (Error) e;
		} else if (e instanceof IllegalAccessException ||
			e instanceof IllegalArgumentException ||
			e instanceof NoSuchMethodException) {
			return new IllegalArgumentException(e);
		} else if (e instanceof InvocationTargetException) {
			return throwsRuntimeException(((InvocationTargetException) e).getTargetException());
		} else if (e instanceof RuntimeException) {
			return (RuntimeException) e;
		} else if (e instanceof InterruptedException) {
			Thread.currentThread().interrupt();
		}
		return throwsRuntimeException(e);
	}

	/**
	 * 解析异常
	 *
	 * @param wrapped
	 * @return
	 */
	public static Throwable unwrap(Throwable wrapped) {
		Throwable unwrapped = wrapped;
		while (true) {
			if (unwrapped instanceof InvocationTargetException) {
				unwrapped = ((InvocationTargetException) unwrapped).getTargetException();
			} else if (unwrapped instanceof UndeclaredThrowableException) {
				unwrapped = ((UndeclaredThrowableException) unwrapped).getUndeclaredThrowable();
			} else {
				return unwrapped;
			}
		}
	}

	/**
	 * 抛出异常
	 *
	 * @param e
	 * @param <T>
	 * @return
	 * @throws T
	 */
	@SuppressWarnings("unchecked")
	private static <T extends Throwable> T throwsRuntimeException(Throwable e) throws T {
		throw (T) e;
	}
}
