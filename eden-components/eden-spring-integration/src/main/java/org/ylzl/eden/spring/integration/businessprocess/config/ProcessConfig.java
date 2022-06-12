package org.ylzl.eden.spring.integration.businessprocess.config;

import com.google.common.collect.Maps;
import lombok.*;
import lombok.experimental.Accessors;
import org.ylzl.eden.spring.integration.businessprocess.executor.Processor;
import org.ylzl.eden.spring.integration.businessprocess.executor.factory.ProcessorFactory;
import org.ylzl.eden.spring.integration.businessprocess.process.ProcessDefinition;
import org.ylzl.eden.spring.integration.businessprocess.process.ProcessNode;

import java.util.HashMap;
import java.util.Map;

/**
 * 流程配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Data
public class ProcessConfig {

	public String name;

	public Map<String, ProcessNodeConfig> nodes = Maps.newHashMapWithExpectedSize(16);

	/**
	 * 添加流程节点配置
	 *
	 * @param processNodeConfig
	 */
	public void addNode(ProcessNodeConfig processNodeConfig) {
		if (nodes.containsKey(processNodeConfig.getName())) {
			throw new ProcessConfigException("The same process cannot define multiple nodes with the same name");
		}
		nodes.put(processNodeConfig.getName(), processNodeConfig);
	}

	/**
	 * 检查配置
	 */
	public void check() {
		int startNodeCount = 0;
		for (ProcessNodeConfig processNodeConfig : nodes.values()) {
			String className = processNodeConfig.getClassName();
			try {
				Class.forName(className);
			} catch (Throwable e) {
				throw new ProcessConfigException("Can not load class '" + className + "' from '" + processNodeConfig.getName() + "'");
			}

			String nextNode = processNodeConfig.getNextNode();
			if (nextNode != null) {
				String[] nextNodes = nextNode.split(",");
				for (String nodeName : nextNodes) {
					if (!nodes.containsKey(nodeName)) {
						throw new ProcessConfigException("Process node '" + nodeName + "' is not found");
					}
				}
			}

			if (processNodeConfig.isBegin()) {
				startNodeCount++;
				if (startNodeCount > 1) {
					throw new ProcessConfigException("Invalid process node due to more than one begin node");
				}
			}
		}
	}

	/**
	 * 构建流程定义
	 *
	 * @param factory
	 * @return
	 * @throws Exception
	 */
	public ProcessDefinition build(ProcessorFactory factory) {
		Map<String, ProcessNode> processNodeMap = new HashMap<>();
		ProcessDefinition processDefinition = new ProcessDefinition();
		processDefinition.setName(name);

		for (ProcessNodeConfig config : nodes.values()) {
			String className = config.getClassName();
			Processor processor;
			try {
				processor = factory.newInstance(className, config.getName());
			} catch (Exception e) {
				throw new ProcessConfigException(e.getMessage());
			}
			ProcessNode processNode = new ProcessNode();
			processNode.setProcessor(processor);
			processNode.setName(config.getName());
			if (config.isBegin()) {
				processDefinition.setFirst(processNode);
			}
			processNode.setAsync(config.isAsync());
			processNodeMap.put(config.getName(), processNode);
		}

		for (ProcessNode processNode : processNodeMap.values()) {
			String nextNodeStr = nodes.get(processNode.getName()).getNextNode();
			if (nextNodeStr == null) {
				continue;
			}
			String[] nextNodes = nextNodeStr.split(",");
			for (String nextNode : nextNodes) {
				processNode.addNextNode(processNodeMap.get(nextNode));
			}
		}

		return processDefinition;
	}
}
