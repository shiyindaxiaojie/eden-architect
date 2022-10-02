package org.ylzl.eden.flow.compose;

import org.ylzl.eden.flow.compose.config.parser.ProcessParser;
import org.ylzl.eden.flow.compose.process.ProcessContext;
import org.ylzl.eden.flow.compose.process.factory.ProcessContextFactory;

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
