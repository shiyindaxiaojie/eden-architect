package org.ylzl.eden.spring.integration.bpc.process;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * 流程定义
 *
 * @author <a href="mailto:guoyuanlu@puyiwm.com">gyl</a>
 * @since 1.0.0
 */
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Data
public class ProcessDefinition {

	/**
	 * 流程定义名称
	 */
	private String name;

	/**
	 * 初始节点
	 */
	private ProcessNode firstProcessNode;

	/**
	 * 设置首节点
	 *
	 * @param processNode
	 */
	public void setFirst(ProcessNode processNode) {
		this.firstProcessNode = processNode;
		if (processNode.hasRing()) {
			throw new ProcessDefinitionException("Processor definition chain has ring");
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		buildStr(sb, firstProcessNode);
		return sb.toString();
	}

	private void buildStr(StringBuilder sb, ProcessNode node) {
		for (ProcessNode processNode : node.getNextNodes().values()) {
			sb.append(node.getName()).append(" -> ").append(processNode.getName()).append("\n");
			buildStr(sb, processNode);
		}
	}
}
