package org.ylzl.eden.spring.integration.businessprocess;

import org.ylzl.eden.spring.integration.businessprocess.config.parser.ProcessParser;
import org.ylzl.eden.spring.integration.businessprocess.process.ProcessContext;
import org.ylzl.eden.spring.integration.businessprocess.process.factory.ProcessContextFactory;

/**
 * 流程引擎
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class ProcessEngine {

	private final ProcessContextFactory processContextFactory;

	public ProcessEngine(ProcessParser processParser) {
		this.processContextFactory = new ProcessContextFactory(processParser);
	}

	public <T> ProcessContext<T> getContext(String name) {
		return processContextFactory.getContext(name);
	}
}
