package org.ylzl.eden.spring.integration.leaf.segement.model;

import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * ID分配模型
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
public class LeafAlloc {

	/**
	 * Key
	 */
	private String key;

	/**
	 * 最大ID
	 */
	private long maxId;

	/**
	 * 步长
	 */
	private int step;

	/**
	 * 更新时间
	 */
	private LocalDateTime updateTime;
}
