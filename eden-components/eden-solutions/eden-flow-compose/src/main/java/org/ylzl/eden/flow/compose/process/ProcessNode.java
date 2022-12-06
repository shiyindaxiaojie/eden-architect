package org.ylzl.eden.flow.compose.process;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.*;
import lombok.experimental.Accessors;
import org.ylzl.eden.flow.compose.exception.ProcessNodeException;
import org.ylzl.eden.flow.compose.core.DynamicProcessor;
import org.ylzl.eden.flow.compose.core.Processor;
import org.ylzl.eden.commons.collections.MapUtils;

import java.util.Map;
import java.util.Set;

/**
 * 流程节点
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
public class ProcessNode {

	/**
	 * 流程节点名称
	 */
	private String name;

	/**
	 * 流程执行器
	 */
	private Processor processor;

	/**
	 * 是否异步调用
	 */
	private boolean async = false;

	/**
	 * 下一个流程节点集合
	 */
	private Map<String, ProcessNode> nextNodes = Maps.newHashMapWithExpectedSize(16);

	/**
	 * 下一个同步的流程节点只能出现一次
	 */
	private boolean hasSyncNextNode = false;

	/**
	 * 添加下一个流程节点
	 *
	 * @param processNode
	 */
	public void addNextNode(ProcessNode processNode) {
		if (processNode.getName().equals(name)) {
			throw new ProcessNodeException("Duplicate node '" + name + "'");
		}

		if (nextNodes.containsKey(processNode.getName())) {
			throw new ProcessNodeException("Node '" + name + "' is already exists with next nodes");
		}

		boolean isAsync = processNode.isAsync();
		boolean isDynamic = processor instanceof DynamicProcessor;
		if (hasSyncNextNode && !isDynamic && !isAsync) {
			throw new ProcessNodeException("Node '" + name + "' has synchronously called successor");
		}

		if (!isAsync) {
			hasSyncNextNode = true;
		}
		nextNodes.put(processNode.getName(), processNode);
	}

	/**
	 * 判断是否有环
	 *
	 * @return
	 */
	public boolean hasRing() {
		return hasRing(this, Sets.newHashSet());
	}

	/**
	 * 判断是否有环
	 *
	 * @param node
	 * @param nodeIds
	 * @return
	 */
	private boolean hasRing(ProcessNode node, Set<String> nodeIds) {
		Map<String, ProcessNode> nextNodes = node.getNextNodes();
		if (MapUtils.isEmpty(nextNodes)) {
			return false;
		}

		nodeIds.add(node.getName());
		boolean isExists = false;
		for (ProcessNode value : nextNodes.values()) {
			if (nodeIds.contains(value.getName())) {
				return true;
			}

			nodeIds.add(value.getName());
			isExists = isExists || hasRing(value, Sets.newHashSet(nodeIds));
		}
		return isExists;
	}
}
