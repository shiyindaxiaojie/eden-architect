package org.ylzl.eden.spring.integration.bpc.process.factory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.AsyncTaskExecutor;
import org.ylzl.eden.spring.integration.bpc.config.env.ProcessConfig;
import org.ylzl.eden.spring.integration.bpc.executor.factory.ProcessorFactory;
import org.ylzl.eden.spring.integration.bpc.process.ProcessContext;
import org.ylzl.eden.spring.integration.bpc.process.ProcessDefinition;
import org.ylzl.eden.spring.integration.bpc.process.ProcessDefinitionException;

import java.util.List;
import java.util.Map;

/**
 * 流程上下文工厂
 *
 * @author <a href="mailto:guoyuanlu@puyiwm.com">gyl</a>
 * @since 1.0.0
 */
@AllArgsConstructor
@Slf4j
public class ProcessContextFactory {

	private final Map<String, ProcessDefinition> processDefinitionMap = Maps.newConcurrentMap();

	private final ProcessorFactory processorFactory;

	private final AsyncTaskExecutor asyncTaskExecutor;

	private List<ProcessConfig> processConfigs = Lists.newCopyOnWriteArrayList();

	public void init() throws Exception {
		for (ProcessConfig processConfig : processConfigs) {
			processConfig.check();
			ProcessDefinition processDefinition = processConfig.build(processorFactory);
			processDefinitionMap.put(processDefinition.getName(), processDefinition);
		}
	}

	public void refresh(List<ProcessConfig> processConfigs) throws Exception {
		this.processConfigs.clear();
		this.processConfigs.addAll(processConfigs);
		init();
	}

	public ProcessContext getContext(String name) {
		ProcessDefinition processDefinition = processDefinitionMap.get(name);
		if (processDefinition == null) {
			throw new ProcessDefinitionException("Process definition '" + name + "' not found");
		}
		return new ProcessContext(processDefinition, asyncTaskExecutor);
	}
}
