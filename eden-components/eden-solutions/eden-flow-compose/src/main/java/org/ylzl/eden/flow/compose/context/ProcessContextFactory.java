package org.ylzl.eden.flow.compose.context;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.ylzl.eden.flow.compose.exception.ProcessDefinitionException;
import org.ylzl.eden.flow.compose.node.ProcessConfig;
import org.ylzl.eden.flow.compose.parser.ProcessParser;
import org.ylzl.eden.flow.compose.process.ProcessDefinition;
import org.ylzl.eden.flow.compose.factory.ProcessorFactory;
import org.ylzl.eden.flow.compose.factory.ReflectProcessorFactory;

import java.util.List;
import java.util.Map;

/**
 * 流程上下文工厂
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Slf4j
public class ProcessContextFactory {

	private static final ProcessorFactory DEFAULT_PROCESSOR_FACTORY = new ReflectProcessorFactory();

	private final Map<String, ProcessDefinition> processDefinitionMap = Maps.newConcurrentMap();

	private final ProcessorFactory processorFactory;

	private ProcessParser processParser;

	public ProcessContextFactory(ProcessParser processParser) {
		this(DEFAULT_PROCESSOR_FACTORY, processParser);
	}

	public ProcessContextFactory(ProcessorFactory processorFactory, ProcessParser processParser) {
		this.processorFactory = processorFactory;
		this.processParser = processParser;
		init();
	}

	public void init() {
		List<ProcessConfig> processConfigs = processParser.parse();
		for (ProcessConfig processConfig : processConfigs) {
			processConfig.check();
			ProcessDefinition processDefinition = processConfig.build(processorFactory);
			processDefinitionMap.put(processDefinition.getName(), processDefinition);
		}
	}

	public void refresh(ProcessParser processParser) throws Exception {
		synchronized (this) {
			this.processParser = processParser;
			init();
		}
	}

	public <T> ProcessContext<T> getContext(String name) {
		ProcessDefinition processDefinition = processDefinitionMap.get(name);
		if (processDefinition == null) {
			throw new ProcessDefinitionException("Process definition '" + name + "' not found");
		}
		return new ProcessContext<T>(processDefinition);
	}
}
