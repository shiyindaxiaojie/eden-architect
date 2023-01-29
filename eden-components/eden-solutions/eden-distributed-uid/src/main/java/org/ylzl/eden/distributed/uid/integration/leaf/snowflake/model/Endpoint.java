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

package org.ylzl.eden.distributed.uid.integration.leaf.snowflake.model;

import lombok.*;
import org.ylzl.eden.spring.framework.json.support.JSONHelper;

import java.io.Serializable;

/**
 * 上报端点
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Endpoint implements Serializable {

	private String ip;

	private Integer port;

	private Long timestamp;

	public static String build(String ip, int port) {
		Endpoint endpoint = new Endpoint(ip, port, System.currentTimeMillis());
		return JSONHelper.json().toJSONString(endpoint);
	}

	public static Endpoint parse(String json) {
		return JSONHelper.json().parseObject(json, Endpoint.class);
	}
}
