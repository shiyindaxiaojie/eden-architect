package org.ylzl.eden.spring.integration.bpc;

import org.ylzl.eden.spring.integration.bpc.node.ProcessContext;
import org.ylzl.eden.spring.integration.bpc.node.factory.ProcessContextFactory;

/**
 * 流程引擎
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class ProcessEngine {

	private final ProcessContextFactory factory;


	public ProcessEngine(ProcessContextFactory factory) {
		this.factory = factory;
	}

	public ProcessContext getContext(String name) {
		return factory.getContext(name);
	}
}
