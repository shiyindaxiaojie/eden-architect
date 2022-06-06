package org.ylzl.eden.spring.integration.bpc.config.model;

import com.google.common.collect.Maps;
import lombok.*;
import lombok.experimental.Accessors;
import org.ylzl.eden.spring.integration.bpc.executor.Processor;
import org.ylzl.eden.spring.integration.bpc.executor.factory.ProcessorFactory;
import org.ylzl.eden.spring.integration.bpc.node.ProcessDefinition;
import org.ylzl.eden.spring.integration.bpc.node.ProcessNode;

import java.util.HashMap;
import java.util.Map;

/**
 * 流程模型
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
public class ProcessModel {

	public String name;

	public Map<String, ProcessNodeModel> nodes = Maps.newHashMapWithExpectedSize(16);

	public void addNode(ProcessNodeModel processNodeModel) {
		if (nodes.containsKey(processNodeModel.getName())) {
			throw new IllegalArgumentException("同一个流程不能定义多个相同id的节点");
		}
		nodes.put(processNodeModel.getName(), processNodeModel);
	}

	public void check() {
		int startNodeCount = 0;
		for (ProcessNodeModel processNodeModel : nodes.values()) {
			String className = processNodeModel.getClassName();
			try {
				Class.forName(className);
			} catch (Throwable e) {
				throw new IllegalArgumentException("Can not load class '" + className + "' from '" + processNodeModel.getName() + "'");
			}

			String nextNode = processNodeModel.getNextNode();
			if (nextNode != null) {
				String[] nextNodes = nextNode.split(",");
				for (String nodeName : nextNodes) {
					if (!nodes.containsKey(nodeName)) {
						throw new IllegalArgumentException("Process node '" + nodeName + "' is not found");
					}
				}
			}

			if (processNodeModel.isBegin() && startNodeCount++ > 1) {
				break;
			}
		}

		if (startNodeCount != 1) {
			throw new IllegalArgumentException("Invalid process node due to more than one start-node");
		}
	}

	public ProcessDefinition build(ProcessorFactory factory) throws Exception {
		Map<String, ProcessNode> processNodeMap = new HashMap<>();
		ProcessDefinition processDefinition = new ProcessDefinition();
		processDefinition.setName(name);

		for (ProcessNodeModel processNodeModel : nodes.values()) {
			String className = processNodeModel.getClassName();
			Processor processor = factory.newInstance(className, processNodeModel.getName());
			ProcessNode processNode = new ProcessNode();
			processNode.setProcessor(processor);
			processNode.setName(processNodeModel.getName());
			if (processNodeModel.isBegin()) {
				processDefinition.setFirst(processNode);
			}
			processNode.setAsyncInvokeNextNode(processNodeModel.isAsyncInvokeNextNode());
			processNodeMap.put(processNodeModel.getName(), processNode);
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
