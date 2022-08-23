package org.ylzl.eden.bussiness.process.choreography;

import org.ylzl.eden.bussiness.process.choreography.config.parser.ProcessParser;
import org.ylzl.eden.bussiness.process.choreography.process.ProcessContext;
import org.ylzl.eden.bussiness.process.choreography.process.factory.ProcessContextFactory;

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
