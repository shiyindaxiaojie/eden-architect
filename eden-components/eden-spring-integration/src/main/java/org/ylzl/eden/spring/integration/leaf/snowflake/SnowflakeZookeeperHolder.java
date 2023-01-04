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

package org.ylzl.eden.spring.integration.leaf.snowflake;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.ylzl.eden.spring.framework.json.support.JSONHelper;

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
		return JSONHelper.json().toJSONString(Endpoint.builder().ip(ip).port(port).timestamp(System.currentTimeMillis()).build());
	}

	private Endpoint deBuildData(String json) throws IOException {
		return JSONHelper.json().parseObject(json, Endpoint.class);
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
