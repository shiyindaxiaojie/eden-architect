package org.ylzl.eden.spring.integration.bpc.config.model;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * 流程节点模型
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
public class ProcessNodeModel {

	private String name;

	private String className;

	private String nextNode;

	private boolean begin = false;

	private boolean asyncInvokeNextNode = false;
}
