package org.ylzl.eden.spring.integration.bpc;

import org.ylzl.eden.spring.integration.bpc.node.ProcessContextFactory;

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
}
