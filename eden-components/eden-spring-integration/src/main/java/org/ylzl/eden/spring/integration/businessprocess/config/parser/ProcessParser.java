package org.ylzl.eden.spring.integration.businessprocess.config.parser;

import org.ylzl.eden.spring.integration.businessprocess.config.ProcessConfig;

import java.util.List;

/**
 * 流程解析器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public interface ProcessParser {

	/**
	 * 解析流程模型
	 *
	 * @return
	 * @throws Exception
	 */
	List<ProcessConfig> parse();
}
