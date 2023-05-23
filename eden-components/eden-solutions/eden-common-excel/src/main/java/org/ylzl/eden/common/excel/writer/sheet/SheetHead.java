package org.ylzl.eden.common.excel.writer.sheet;

import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * Sheet 头部模型
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Data
public class SheetHead {

	/**
	 * 自定义头部
	 */
	private List<List<String>> head;

	/**
	 * 忽略字段
	 */
	private Set<String> ignoreHeadFields;
}
