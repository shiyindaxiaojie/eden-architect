package org.ylzl.eden.common.excel.writer.sheet;

/**
 * Sheet 头部生成器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public interface SheetHeadGenerator {

	/**
	 * 自定义头部信息
	 *
	 * @param clazz 目标类
	 * @return 头部信息
	 */
	SheetHead head(Class<?> clazz);
}
