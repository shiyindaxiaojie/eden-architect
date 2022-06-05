package org.ylzl.eden.spring.integration.bpc.config.model;

import com.google.common.collect.Maps;
import lombok.*;
import lombok.experimental.Accessors;

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


}
