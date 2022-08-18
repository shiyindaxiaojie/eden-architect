package org.ylzl.eden.spring.integration.leaf.snowflake;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.ylzl.eden.commons.json.JacksonUtils;

import java.io.IOException;

/**
 * TODO
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Slf4j
public class SnowflakeZookeeperHolder {

	private int workerID;

	private String ip;

	private int port;

	private String connectionString;

	private long lastUpdateTime;

	private String buildEndpoint() throws JsonProcessingException {
		return JacksonUtils.toJSONString(Endpoint.builder().ip(ip).port(port).timestamp(System.currentTimeMillis()).build());
	}

	private Endpoint deBuildData(String json) throws IOException {
		return JacksonUtils.toObject(json, Endpoint.class);
	}

	@Accessors(chain = true)
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	@EqualsAndHashCode
	@ToString
	@Data
	static class Endpoint {
		private String ip;
		private int port;
		private long timestamp;
	}
}
