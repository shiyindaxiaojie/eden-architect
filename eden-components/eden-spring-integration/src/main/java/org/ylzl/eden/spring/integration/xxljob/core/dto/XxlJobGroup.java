package org.ylzl.eden.spring.integration.xxljob.core.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * XxlJob 执行器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
@Data
public class XxlJobGroup {

	private int id;

	private String appName;

	private String title;

	private int order;

	private int addressType;

	private String addressList;

	private List<String> registryList;

	public List<String> getRegistryList() {
		if (addressList != null && addressList.trim().length() > 0) {
			registryList = new ArrayList<>(Arrays.asList(addressList.split(",")));
		}
		return registryList;
	}
}
