package org.ylzl.eden.spring.integration.bpc;

import org.ylzl.eden.spring.integration.bpc.config.parser.ProcessParser;
import org.ylzl.eden.spring.integration.bpc.process.ProcessContext;
import org.ylzl.eden.spring.integration.bpc.process.factory.ProcessContextFactory;

/**
 * 流程引擎
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class ProcessEngine {

	private final ProcessContextFactory processContextFactory;

	public ProcessEngine(ProcessParser processParser) {
		this.processContextFactory = new ProcessContextFactory(processParser);;
	}

	public ProcessContext getContext(String name) {
		return processContextFactory.getContext(name);
	}
}
