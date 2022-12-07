package org.ylzl.eden.flow.compose.parser.element;

import com.google.common.collect.Maps;
import lombok.*;
import lombok.experimental.Accessors;
import org.ylzl.eden.flow.compose.Processor;
import org.ylzl.eden.flow.compose.exception.ProcessConfigException;
import org.ylzl.eden.flow.compose.model.ProcessDefinition;
import org.ylzl.eden.flow.compose.model.ProcessNode;
import org.ylzl.eden.flow.compose.processor.ProcessorFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 流程配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Data
public class ProcessElement {

	public String name;

	public Map<String, NodeElement> nodes = Maps.newHashMapWithExpectedSize(16);

	/**
	 * 添加流程节点配置
	 *
	 * @param nodeElement
	 */
	public void addNode(NodeElement nodeElement) {
		if (nodes.containsKey(nodeElement.getName())) {
			throw new ProcessConfigException("The same process cannot define multiple nodes with the same name");
		}
		nodes.put(nodeElement.getName(), nodeElement);
	}

	/**
	 * 检查配置
	 */
	public void check() {
		int startNodeCount = 0;
		for (NodeElement nodeElement : nodes.values()) {
			String className = nodeElement.getClassName();
			try {
				Class.forName(className);
			} catch (Throwable e) {
				throw new ProcessConfigException("Can not load class '" + className + "' from '" + nodeElement.getName() + "'");
			}

			String nextNode = nodeElement.getNextNode();
			if (nextNode != null) {
				String[] nextNodes = nextNode.split(",");
				for (String nodeName : nextNodes) {
					if (!nodes.containsKey(nodeName)) {
						throw new ProcessConfigException("Process node '" + nodeName + "' is not found");
					}
				}
			}

			if (nodeElement.isBegin()) {
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

		for (NodeElement node : nodes.values()) {
			String className = node.getClassName();
			Processor processor;
			try {
				processor = factory.newInstance(className, node.getName());
			} catch (Exception e) {
				throw new ProcessConfigException(e.getMessage(), e);
			}
			ProcessNode processNode = new ProcessNode();
			processNode.setProcessor(processor);
			processNode.setName(node.getName());
			if (node.isBegin()) {
				processDefinition.setFirst(processNode);
			}
			processNode.setAsync(node.isAsync());
			processNodeMap.put(node.getName(), processNode);
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
