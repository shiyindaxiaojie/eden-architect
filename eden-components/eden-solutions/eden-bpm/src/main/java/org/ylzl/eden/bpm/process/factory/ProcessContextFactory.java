package org.ylzl.eden.bpm.process.factory;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.ylzl.eden.bpm.executor.factory.ProcessorFactory;
import org.ylzl.eden.bpm.process.ProcessDefinitionException;
import org.ylzl.eden.bpm.config.ProcessConfig;
import org.ylzl.eden.bpm.config.parser.ProcessParser;
import org.ylzl.eden.bpm.executor.factory.ReflectProcessorFactory;
import org.ylzl.eden.bpm.process.ProcessContext;
import org.ylzl.eden.bpm.process.ProcessDefinition;

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

	private static final AsyncTaskExecutor DEFAULT_ASYNC_TASK_EXECUTOR = new SimpleAsyncTaskExecutor();

	private final Map<String, ProcessDefinition> processDefinitionMap = Maps.newConcurrentMap();

	private final ProcessorFactory processorFactory;

	private final AsyncTaskExecutor asyncTaskExecutor;

	private ProcessParser processParser;

	public ProcessContextFactory(ProcessParser processParser) {
		this(DEFAULT_PROCESSOR_FACTORY, DEFAULT_ASYNC_TASK_EXECUTOR, processParser);
	}

	public ProcessContextFactory(ProcessorFactory processorFactory, AsyncTaskExecutor asyncTaskExecutor, ProcessParser processParser) {
		this.processorFactory = processorFactory;
		this.asyncTaskExecutor = asyncTaskExecutor;
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
		return new ProcessContext<T>(processDefinition, asyncTaskExecutor);
	}
}
