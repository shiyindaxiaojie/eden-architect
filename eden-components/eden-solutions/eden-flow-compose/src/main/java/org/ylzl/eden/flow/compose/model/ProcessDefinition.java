/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ylzl.eden.flow.compose.model;

import lombok.*;
import lombok.experimental.Accessors;
import org.ylzl.eden.flow.compose.exception.ProcessDefinitionException;

/**
 * 流程定义
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
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
