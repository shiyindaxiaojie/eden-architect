package org.ylzl.eden.extension.compile;

import org.ylzl.eden.extension.SPI;

/**
 * 编译
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@SPI("javassist")
public interface Compiler {

	Class<?> compile(String code, ClassLoader classLoader);
}
