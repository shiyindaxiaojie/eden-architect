package org.ylzl.eden.distributed.uid.integration.leaf.snowflake.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.ylzl.eden.commons.net.IpConfigUtils;

import java.io.Serializable;

/**
 * 应用信息
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
public class App implements Serializable {

	private String ip;

	private Integer port;

	public String getIp() {
		if (ip == null) {
			return IpConfigUtils.getIpAddress();
		}
		return ip;
	}
}
