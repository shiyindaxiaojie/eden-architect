package org.ylzl.eden.spring.integration.bpc.process.factory;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.ylzl.eden.spring.integration.bpc.config.env.ProcessConfig;
import org.ylzl.eden.spring.integration.bpc.config.parser.ProcessParser;
import org.ylzl.eden.spring.integration.bpc.executor.factory.ProcessorFactory;
import org.ylzl.eden.spring.integration.bpc.executor.factory.ReflectProcessorFactory;
import org.ylzl.eden.spring.integration.bpc.process.ProcessContext;
import org.ylzl.eden.spring.integration.bpc.process.ProcessDefinition;
import org.ylzl.eden.spring.integration.bpc.process.ProcessDefinitionException;

import java.util.List;
import java.util.Map;

/**
 * 流程上下文工厂
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Slf4j
public class ProcessContextFactory {

	private static final ProcessorFactory DEFAULT_PROCESSOR_FACTORY = new ReflectProcessorFactory();

	private static final AsyncTaskExecutor DEFAULT_ASYNC_TASK_EXECUTOR = new SimpleAsyncTaskExecutor();

	private final Map<String, ProcessDefinition> processDefinitionMap = Maps.newConcurrentMap();

	private final ProcessorFactory processorFactory;

	private final AsyncTaskExecutor asyncTaskExecutor;

	private ProcessParser processParser;

	public ProcessContextFactory(ProcessParser processParser) {
		this.processorFactory = DEFAULT_PROCESSOR_FACTORY;
		this.asyncTaskExecutor = DEFAULT_ASYNC_TASK_EXECUTOR;
		this.processParser = processParser;
	}

	public ProcessContextFactory(ProcessorFactory processorFactory, AsyncTaskExecutor asyncTaskExecutor, ProcessParser processParser) {
		this.processorFactory = processorFactory;
		this.asyncTaskExecutor = asyncTaskExecutor;
		this.processParser = processParser;
	}

	public void init() throws Exception {
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

	public ProcessContext getContext(String name) {
		ProcessDefinition processDefinition = processDefinitionMap.get(name);
		if (processDefinition == null) {
			throw new ProcessDefinitionException("Process definition '" + name + "' not found");
		}
		return new ProcessContext(processDefinition, asyncTaskExecutor);
	}
}
