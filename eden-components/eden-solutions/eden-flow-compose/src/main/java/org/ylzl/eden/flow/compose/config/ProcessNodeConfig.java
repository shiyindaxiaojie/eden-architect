package org.ylzl.eden.flow.compose.config;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * 流程节点配置
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
public class ProcessNodeConfig {

	private String name;

	private String className;

	private String nextNode;

	private boolean begin = false;

	private boolean async = false;
}
