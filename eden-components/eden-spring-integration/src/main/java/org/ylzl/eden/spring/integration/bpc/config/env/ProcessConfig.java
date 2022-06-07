package org.ylzl.eden.spring.integration.bpc.config.env;

import com.google.common.collect.Maps;
import lombok.*;
import lombok.experimental.Accessors;
import org.ylzl.eden.spring.integration.bpc.executor.Processor;
import org.ylzl.eden.spring.integration.bpc.executor.factory.ProcessorFactory;
import org.ylzl.eden.spring.integration.bpc.process.ProcessDefinition;
import org.ylzl.eden.spring.integration.bpc.process.ProcessNode;

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

	public void addNode(ProcessNodeConfig processNodeConfig) {
		if (nodes.containsKey(processNodeConfig.getName())) {
			throw new IllegalArgumentException("同一个流程不能定义多个相同id的节点");
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

			if (processNodeConfig.isBegin() && startNodeCount++ > 1) {
				break;
			}
		}

		if (startNodeCount != 1) {
			throw new ProcessConfigException("Invalid process node due to more than one start-node");
		}
	}

	/**
	 * 构建流程定义
	 *
	 * @param factory
	 * @return
	 * @throws Exception
	 */
	public ProcessDefinition build(ProcessorFactory factory) throws Exception {
		Map<String, ProcessNode> processNodeMap = new HashMap<>();
		ProcessDefinition processDefinition = new ProcessDefinition();
		processDefinition.setName(name);

		for (ProcessNodeConfig processNodeConfig : nodes.values()) {
			String className = processNodeConfig.getClassName();
			Processor processor = factory.newInstance(className, processNodeConfig.getName());
			ProcessNode processNode = new ProcessNode();
			processNode.setProcessor(processor);
			processNode.setName(processNodeConfig.getName());
			if (processNodeConfig.isBegin()) {
				processDefinition.setFirst(processNode);
			}
			processNode.setSyncInvokeNextNode(processNodeConfig.isSyncInvokeNextNode());
			processNodeMap.put(processNodeConfig.getName(), processNode);
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
